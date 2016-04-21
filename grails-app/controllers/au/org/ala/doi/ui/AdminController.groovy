package au.org.ala.doi.ui

import au.org.ala.doi.DoiService
import au.org.ala.doi.util.DoiProvider
import grails.converters.JSON
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

class AdminController {

    DoiService doiService

    def index() {
        // Only used to render admin main page
    }

    def mintDoi() {
        // Only used to render mint DOI page
    }

    /**
     * Mint a new DOI or register and existing DOI within the DOI Service
     *
     */
    def createDoi() {
        try {
            MultipartFile file = null
            if (request instanceof MultipartHttpServletRequest) {
                file = request.getFile(request.fileNames[0])
                log.debug("File ${file}")
                if (!file?.size) {
                    // Ignore empty files, looks like we are getting an empty file for the missing file parameter rather than no file at all.                 //
                    file = null
                }
            }

            def applicationMetadata
            if (params?.applicationMetadata && params.applicationMetadata.trim()) {
                applicationMetadata = JSON.parse(params.applicationMetadata)
            }

            Map result

            if (params?.newExistingDoiRadio == "existing") {
                def providerMetadata = JSON.parse(params.providerMetadata)

                result = doiService.mintDoi(
                        DoiProvider.byName(params.provider),
                        providerMetadata,
                        params.title,
                        params.authors,
                        params.description,
                        params.applicationUrl,
                        params.fileUrl,
                        file,
                        applicationMetadata,
                        params.customLandingPageUrl)
            } else {
                result = doiService.registerDoi(
                        params.existingDoi,
                        params.title,
                        params.authors,
                        params.description,
                        params.applicationUrl,
                        params.fileUrl,
                        file,
                        applicationMetadata,
                        params.customLandingPageUrl)
            }

            log.debug("Result: ${result}")
            if (result?.status == 'ok') {
                redirect(url: result.doiServiceLandingPage)
            } else {
                throw new Exception(result?.error)
            }

        }
        catch (e) {
            log.error("Error while trying to mint a DOI from UI: ${e}",e)
            def errorMessage = e?.cause.message ?: e.message
            render view: "mintDoi", model: [status:"error", errorMessage: errorMessage, mintParameters: params]
        }
    }
}
