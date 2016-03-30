package au.org.ala.doi.ws

import au.ala.org.ws.security.RequireApiKey
import au.org.ala.doi.FileService
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import static au.org.ala.doi.util.Utils.isUuid

import au.org.ala.doi.BaseController
import au.org.ala.doi.Doi
import au.org.ala.doi.util.DoiProvider
import au.org.ala.doi.DoiService
import grails.converters.JSON

@RequireApiKey
class DoiController extends BaseController {

    DoiService doiService
    FileService fileService

    /**
     * Mint a new DOI. POST only. Must have an ALA API Key.
     *
     * This endpoint accepts 2 formats:
     * <ol>
     *     <li>A Multipart Request, where the metadata is in a parameter called 'json' and the file associated with the DOI is provided in the request; or
     *     <li>A standard post with a JSON body, with a mandatory 'fileUrl' property containing a URL where the file for the DOI can be downloaded from.
     * </ol>
     *
     * The request must have JSON object with the following structure:
     * <pre>
     * {
     *     provider: "ANDS", // the doi provider to use (see {@link DoiProvider} for a list of supported providers)
     *     applicationUrl: "http://....", // the url to the relevant page on the source application. This is NOT the landing page: it is used to provide a link ON the landing page back to the original source of the publication/data/etc for the DOI.
     *     providerMetadata: { // the provider-specific metadata to be sent with the DOI minting request
     *         ...
     *     },
     *     title: "...", // title to be displayed on the landing page
     *     authors: "...", // author(s) to be displayed on the landing page
     *     description: "...", // description to be displayed on the landing page
     *
     *
     *     // the following are optional
     *     fileUrl: "http://....", // the url to use to download the file for the DOI (use this, or send the file as a multipart request)
     *     customLandingPageUrl: "http://...", // an application-specific landing page that you want the DOI to resolve to. If not provided, the default ALA-DOI landing page will be used.
     *     applicationMetadata: { // any application-specific metadata you want to display on the landing page in ALA-DOI
     *         ...
     *     }
     * }
     * </pre>
     *
     * If "fileUrl" is not provided, then you must send the file in a multipart request with the metadata as a JSON string in a form part called 'json'.
     *
     * @return JSON response containing the DOI and the landing page on success, HTTP 500 on failure
     */
    def mintDoi() {
        Map json = getJson(request)

        if (validateMintRequest(request)) {
            MultipartFile file = null
            if (request instanceof MultipartHttpServletRequest) {
                file = request.getFile(request.fileNames[0])
            }

            Map result = doiService.mintDoi(DoiProvider.byName(json.provider), json.providerMetadata, json.title,
                    json.authors, json.description, json.applicationUrl, json.fileUrl, file, json.applicationMetadata,
                    json.customLandingPageUrl)

            render result as JSON
        }
    }

    private static Map getJson(request) {
        Map json = request.getJSON()

        if (!json && request instanceof MultipartHttpServletRequest) {
            json = JSON.parse(request.getParameter("json")) as Map
        }

        json
    }

    private boolean validateMintRequest(request) {
        boolean valid = true

        Map json = getJson(request)

        if (areMandatoryMetadataFieldsMissing(json) || areFileAndUrlMissing(json, request)) {
            log.debug("Rejecting request with missing mandatory parameters. Provided parameters: ${params}")
            badRequest "provider, title, authors, description, applicationUrl and providerMetadata must be provided " +
                    "in the request's JSON body, and you must either provide a fileUrl or a multipart request"

            valid = false
        } else if (!DoiProvider.byName(json.provider)) {
            log.debug("Rejecting request with invalid provider ${json.provider}")
            badRequest "invalid provider: must be one of ${DoiProvider.values()*.name().join(", ")}"

            valid = false
        }

        valid
    }

    private static boolean areMandatoryMetadataFieldsMissing(json) {
        !json.provider || !json.applicationUrl || !json.providerMetadata || !json.title || !json.authors || !json.description
    }

    private static boolean areFileAndUrlMissing(json, request) {
        !json.fileUrl && (!(request instanceof MultipartHttpServletRequest) || !request.fileNames)
    }

    /**
     * Retrieve the metadata for a doi by either UUID or DOI
     *
     * @return JSON response containing the metadata for the requested doi
     */
    def getDoi() {
        if (!params.id) {
            badRequest "id is a required parameter"
        } else {
            Doi doi = isUuid(params.id) ? doiService.findByUuid(params.id) : doiService.findByDoi(params.id)

            if (!doi) {
                notFound "No doi was found for ${params.id}"
            } else {
                render doi as JSON
            }
        }
    }

    /**
     * Retrieve the file for a doi by either UUID or DOI
     *
     * @return the file associated with the DOI
     */
    def download() {
        if (!params.id) {
            badRequest "id is a required parameter"
        } else {
            Doi doi = isUuid(params.id) ? doiService.findByUuid(params.id) : doiService.findByDoi(params.id)

            if (!doi) {
                notFound "No doi was found for ${params.id}"
            } else {
                File file = fileService.getFileForDoi(doi)
                if (file) {
                    response.setContentType(doi.contentType)
                    response.setHeader("Content-disposition", "attachment;filename=${file.name}")
                    file.withInputStream {
                        response.outputStream << it
                    }
                    response.outputStream.flush()
                } else {
                    notFound "No file was found for DOI ${doi.doi} (uuid = ${doi.uuid})"
                }
            }
        }
    }
}