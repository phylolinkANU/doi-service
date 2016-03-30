package au.org.ala.doi.ui

import au.org.ala.doi.BaseController
import au.org.ala.doi.Doi
import au.org.ala.doi.DoiService
import au.org.ala.doi.util.Utils

class DoiResolveController extends BaseController {

    static final int DEFAULT_PAGE_SIZE = 25
    DoiService doiService

    def index() {
        int pageSize = params.getInt("pageSize", DEFAULT_PAGE_SIZE)
        int offset = params.getInt("offset", 0)

        render view: "index", model: [dois: doiService.listDois(pageSize, offset), offset: offset, pageSize: pageSize]
    }

    def doi() {
        if (!params.id || !Utils.isUuid(params.id)) {
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
}
