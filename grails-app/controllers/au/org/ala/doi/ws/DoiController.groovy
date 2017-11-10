package au.org.ala.doi.ws

import au.ala.org.ws.security.RequireApiKey
import au.org.ala.doi.BasicWSController
import au.org.ala.doi.exceptions.DoiNotFoundException
import au.org.ala.doi.exceptions.DoiUpdateException
import au.org.ala.doi.exceptions.DoiValidationException
import au.org.ala.doi.storage.Storage
import com.google.common.io.ByteSource
import grails.web.http.HttpHeaders
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest

import javax.servlet.http.HttpServletRequest
import javax.validation.constraints.NotNull

import static au.org.ala.doi.util.Utils.isUuid

import au.org.ala.doi.Doi
import au.org.ala.doi.util.DoiProvider
import au.org.ala.doi.DoiService
import grails.converters.JSON

import static javax.servlet.http.HttpServletResponse.SC_CREATED
import static javax.servlet.http.HttpServletResponse.SC_OK

@RequireApiKey
class DoiController extends BasicWSController {

    static responseFormats = ['json']

    static namespace = "v1"

    DoiService doiService
    Storage storage

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
    def save() {
        Map json = getJson(request)

        if (validateMintRequest(json)) {
            MultipartFile file = null
            if (request instanceof MultipartHttpServletRequest) {
                file = request.getFile(request.fileNames[0])
            }

            Map result = doiService.mintDoi(DoiProvider.byName(json.provider), json.providerMetadata, json.title,
                    json.authors, json.description, json.licence, json.applicationUrl, json.fileUrl, file, json.applicationMetadata,
                    json.customLandingPageUrl, null, json.userId)

            if (result.uuid) {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( method: 'GET', resource: this.controllerName, action: 'show',id: result.uuid, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
            }
            render result as JSON, status: SC_CREATED
        }
    }

    private static Map getJson(HttpServletRequest request) {
        Map json = request.getJSON()

        if (!json && request instanceof MultipartHttpServletRequest) {
            json = JSON.parse(request.getParameter("json")) as Map
        }

        json
    }

    private boolean validateMintRequest(Map json) {
        boolean valid = true

        if (areMandatoryMetadataFieldsMissing(json)) {
            log.debug("Rejecting request with missing mandatory parameters. Provided parameters: ${json}")
            badRequest "provider, title, authors, description, applicationUrl and providerMetadata must be provided " +
                    "in the request's JSON body"

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

    def index(Integer max) {

        max = Math.min(max ?: 10, 100)
        int offset = params.int('offset', 0)
        String sort = params.get('sort', 'dateMinted')
        String order = params.get('order', 'desc')

        String userId = params.get('userId')
        def eqParams = [:]
        if (userId) eqParams << [ userId : userId ]

        def list = doiService.listDois(max, offset, sort, order, eqParams)
        Map result = [:]
        result << [ list: list ]
        result << [ totalCount: list.totalCount ]
        respond result
    }

    /**
     * Retrieve the metadata for a doi by either UUID or DOI
     *
     * @param id Either the local UUID or the DOI identifier
     * @return JSON response containing the metadata for the requested doi
     */
    def show(@NotNull String id) {
        Doi doi = queryForResource(id)

        if (!doi) {
            notFound "No doi was found for ${params.id}"
        } else {
            render doi as JSON
        }
    }

    /**
     * Retrieve the file for a doi by either UUID or DOI
     *
     * @param id Either the local UUID or the DOI identifier
     * @return the file associated with the DOI
     */
    def download(@NotNull String id) {
        Doi doi = queryForResource(id)

        if (!doi) {
            notFound "No doi was found for ${id}"
        } else {
            ByteSource byteSource = storage.getFileForDoi(doi)
            if (byteSource) {
                response.setContentType(doi.contentType)
                response.setHeader("Content-disposition", "attachment;filename=${doi.filename}")
                byteSource.openStream().withStream {
                    response.outputStream << it
                }
                response.outputStream.flush()
            } else {
                notFound "No file was found for DOI ${doi.doi} (uuid = ${doi.uuid})"
            }
        }
    }

    def patch(@NotNull String id) {
        update(id)
    }

    def update(@NotNull String id) {

        def objectToBind = getJson(request)
        MultipartFile file = null
        if (request instanceof MultipartHttpServletRequest) {
            file = request.getFile(request.fileNames[0])
        }

        Doi instance
        try {
            instance = doiService.updateDoi(id, objectToBind, file)
        } catch (DoiNotFoundException e) {
            notFound()
            return
        } catch (DoiUpdateException e) {
            badRequest(e.message)
            return
        } catch (DoiValidationException e) {
            unprocessableEntity()
            return
        }

        response.addHeader(HttpHeaders.LOCATION,
                grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: id, absolute: true,
                        namespace: hasProperty('namespace') ? this.namespace : null ))
        respond instance, [status: SC_OK]
    }

    protected Doi queryForResource(Serializable id) {
        String idString = id instanceof String ? id : id.toString()
        isUuid(idString) ? doiService.findByUuid(idString) : doiService.findByDoi(idString)
    }


}
