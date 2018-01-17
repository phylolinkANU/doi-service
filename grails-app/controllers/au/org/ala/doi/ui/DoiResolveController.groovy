package au.org.ala.doi.ui

import au.org.ala.doi.BasicWSController
import au.org.ala.doi.Doi
import au.org.ala.doi.DoiService
import au.org.ala.doi.storage.Storage
import au.org.ala.ws.validation.constraints.UUID
import com.google.common.io.ByteSource
import org.springframework.web.context.request.RequestContextHolder
import grails.converters.XML

import javax.validation.constraints.NotNull

class DoiResolveController extends BasicWSController {

    static final int DEFAULT_PAGE_SIZE = 20
    DoiService doiService
    Storage storage


    def index() {
        int pageSize = params.getInt("pageSize", DEFAULT_PAGE_SIZE)
        int offset = params.getInt("offset", 0)

        def isAdmin = RequestContextHolder.currentRequestAttributes()?.isUserInRole("ROLE_ADMIN")

        render view: "index", model: [dois    : doiService.listDois(pageSize, offset, "dateMinted", "desc", !isAdmin ? [active:true]:null),
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
}
