package au.org.ala.doi.ui

import au.org.ala.doi.Doi
import au.org.ala.doi.DoiService
import au.org.ala.doi.FileService
import au.org.ala.ws.controller.BasicWSController

import au.org.ala.ws.validation.constraints.UUID
import org.springframework.web.context.request.RequestContextHolder

import javax.validation.constraints.NotNull

class DoiResolveController extends BasicWSController {

    static final int DEFAULT_PAGE_SIZE = 20
    DoiService doiService
    FileService fileService


    def index() {
        int pageSize = params.getInt("pageSize", DEFAULT_PAGE_SIZE)
        int offset = params.getInt("offset", 0)

        def isAdmin = RequestContextHolder.currentRequestAttributes()?.isUserInRole("ROLE_ADMIN")

        render view: "index", model: [dois    : doiService.listDois(pageSize, offset),
                                      offset  : offset,
                                      pageSize: pageSize,
                                      isAdmin : isAdmin]
    }

    def doi(@NotNull @UUID String id) {
        Doi doi = doiService.findByUuid(id)

        if (doi) {
            render view: "doi", model: [doi: doi]
        } else {
            notFound "No DOI record was found for UUID ${id}"
        }
    }

    def download(@NotNull @UUID String id) {
        Doi doi = doiService.findByUuid(id)

        if (!doi) {
            notFound "No doi was found for ${id}"
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
