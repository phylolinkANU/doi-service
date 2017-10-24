package au.org.ala.doi

import au.org.ala.doi.providers.AndsService
import au.org.ala.doi.providers.DoiProviderService
import au.org.ala.doi.util.DoiProvider
import au.org.ala.doi.util.ServiceResponse
import org.springframework.web.multipart.MultipartFile

class DoiService extends BaseDataAccessService {

    def grailsApplication
    AndsService andsService
    FileService fileService
    EmailService emailService

    Map mintDoi(DoiProvider provider, Map providerMetadata, String title, String authors, String description,
                String applicationUrl, String fileUrl, MultipartFile file, Map applicationMetadata = [:],
                String customLandingPageUrl = null, String defaultDoi = null) {
        checkArgument provider
        if(!defaultDoi) {
            checkArgument providerMetadata, "No provider metadata has been sent"
        }
        checkArgument applicationUrl, "No url to the original application has been sent"

        UUID uuid = UUID.randomUUID()

        String contentType = file ? file.contentType : new URL(fileUrl).openConnection().contentType
        Doi entity = new Doi(uuid: uuid, customLandingPageUrl: customLandingPageUrl, dateMinted: new Date(),
                title: title, authors: authors, description: description, provider: provider,
                providerMetadata: providerMetadata, applicationMetadata: applicationMetadata,
                applicationUrl: applicationUrl, filename: file?.originalFilename ?: uuid, contentType: contentType)

        entity.doi = "dummyForValidation" // doi is a mandatory field, so we set a temp value to validate the rest of the entity
        if (entity.validate()) {
            file ? fileService.storeFileForDoi(entity, file) : fileService.storeFileForDoi(entity, fileUrl)

            String uuidString = uuid.toString()
            String doi = defaultDoi ?:  getProviderService(provider).mintDoi(uuidString, providerMetadata, customLandingPageUrl)
            entity.doi = doi

            boolean success = save entity

            Map result
            if (!success) {
                log.error("A DOI was generated successfully through ${provider}, but the DB record failed to save. No default landing page will exist for this DOI!")
                sendPostDOICreationErrorEmail(entity.doi, "<ul><li>${entity.errors.collect().join("</li><li>")}</li></ul>")
                result = [uuid: null, doi: doi, error: "A DOI was generated, but the server failed to save to the local DB. No default landing page will exist for this DOI!", status: "error"]
            } else {
                result = [uuid: uuid, doi: doi, landingPage: getProviderService(provider).generateLandingPageUrl(uuidString, customLandingPageUrl)
                          , doiServiceLandingPage: getProviderService(provider).generateLandingPageUrl(uuidString, null), status: "ok"]
            }

            result
        } else {
            // should never happen, so we can just throw a generic exception which will result in a HTTP 500 response
            throw new IllegalStateException("${entity.errors.allErrors.join(";\n")}")
        }
    }

    void sendPostDOICreationErrorEmail(String doi, String error) {
        emailService.sendEmail("${grailsApplication.config.support.email}", "doiservice<no-reply@ala.org.au>", "Failure Alert - DOI ${doi}",
                """<html><body>
                        <p>An unexpected error occurred after successfully generating the DOI ${doi}.</p>
                        <p>The DOI was generated, but the server failed to save to the local DB. No default landing page will exist for this DOI!</p>
                        ${error}
                        <p>This is an automated email. Please do not reply.</p>
                        </body></html>""")
    }

    Doi findByUuid(String uuid) {
        checkArgument uuid

        Doi.findByUuid(UUID.fromString(uuid))
    }

    Doi findByDoi(String doi) {
        checkArgument doi

        Doi.findByDoi(doi)
    }

    def listDois(int pageSize, int startFrom, String sortBy = "dateMinted", String sortOrder = "desc") {
        def criteria = Doi.createCriteria()
        criteria.list (max: pageSize, offset: startFrom) {
            order(sortBy, sortOrder)
        }
    }

    // Replace this with a factory if/when other DOI providers are supported
    private DoiProviderService getProviderService(DoiProvider provider) {
        DoiProviderService service

        switch (provider) {
            case DoiProvider.ANDS:
                service = andsService
                break
        }

        service
    }
}
