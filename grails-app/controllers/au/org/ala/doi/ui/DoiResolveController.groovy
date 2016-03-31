package au.org.ala.doi.ui

import au.org.ala.doi.BaseController
import au.org.ala.doi.Doi
import au.org.ala.doi.DoiService
import au.org.ala.doi.FileService
import au.org.ala.doi.util.Utils

import static au.org.ala.doi.util.Utils.isUuid

class DoiResolveController extends BaseController {

    static final int DEFAULT_PAGE_SIZE = 25
    DoiService doiService
    FileService fileService

    def index() {
        int pageSize = params.getInt("pageSize", DEFAULT_PAGE_SIZE)
        int offset = params.getInt("offset", 0)

        render view: "index", model: [dois: doiService.listDois(pageSize, offset), offset: offset, pageSize: pageSize]
    }

    def doi() {
        if (!params.id || !isUuid(params.id)) {
            badRequest "id must be provided, and must be a valid UUID"
        } else {
            Doi doi = doiService.findByUuid(params.id)

            if (doi) {
                render view: "doi", model: [doi: doi]
            } else {
                notFound "No DOI record was found for UUID ${params.id}"
            }
        }
    }

    def download() {
        if (!params.id || !isUuid(params.id)) {
            badRequest "id must be provided, and must be a valid UUID"
        } else {
            Doi doi = doiService.findByUuid(params.id)

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
