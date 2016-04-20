package au.org.ala.doi.ui

import au.org.ala.doi.DoiService
import au.org.ala.doi.util.DoiProvider
import grails.converters.JSON
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

class AdminController {

    DoiService doiService

    def index() {
        log.debug("12:41 Here Again")
//        render 'Hello World'
    }

    def mintDoi() {
        log.debug("13:22 Mint Doi")
//        render 'Hello World'


    }

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
     *{*     provider: "ANDS", // the doi provider to use (see {@link DoiProvider} for a list of supported providers)
     *     applicationUrl: "http://....", // the url to the relevant page on the source application. This is NOT the landing page: it is used to provide a link ON the landing page back to the original source of the publication/data/etc for the DOI.
     *     providerMetadata: { // the provider-specific metadata to be sent with the DOI minting request
     *         ...
     *},
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
     *}*}* </pre>
     *
     * If "fileUrl" is not provided, then you must send the file in a multipart request with the metadata as a JSON string in a form part called 'json'.
     *
     * @return JSON response containing the DOI and the landing page on success, HTTP 500 on failure
     */
    def createDoi() {
        log.debug("16:59")
        log.debug("Params: ${params}")
        log.debug("Request type: ${request.class}")

//        if (validateMintRequest(request)) {
        MultipartFile file = null
        if (request instanceof MultipartHttpServletRequest) {
            file = request.getFile(request.fileNames[0])
            log.debug("File ${file}")
        }

        def applicationMetadata = [foo: 'bar']

        def providerMetadata
        if(params?.providerMetadata)
        {
            providerMetadata = JSON.parse(params.providerMetadata)
        }

        Map result = doiService.mintDoi(
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

//        }

        log.debug("Result: ${result}")
        if(result?.status == 'ok') {
            redirect(url: result.landingPage)
        }
    }
}
