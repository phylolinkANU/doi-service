package au.ala.org.doi.ui

import au.org.ala.doi.Doi
import au.org.ala.doi.DoiService
import au.org.ala.doi.FileService
import au.org.ala.doi.ui.DoiResolveController
import grails.test.mixin.TestFor
import org.apache.http.HttpStatus
import spock.lang.Specification

@TestFor(DoiResolveController)
class DoiResolveControllerSpec extends Specification {

    DoiResolveController controller

    def setup() {
        controller = new DoiResolveController()
        controller.doiService = Mock(DoiService)
        controller.fileService = Mock(FileService)
    }

    def "index should list all DOIs"() {
        setup:
        Doi doi1 = new Doi(uuid: "1")
        Doi doi2 = new Doi(uuid: "2")
        controller.doiService.listDois(_, _) >> [doi1, doi2]
        when:
        controller.index()

        then:
        controller.modelAndView.viewName == "/doiResolve/index"
        controller.modelAndView.model == [dois: [doi1, doi2], offset: 0, pageSize: DoiResolveController.DEFAULT_PAGE_SIZE]
    }

//    def "doi should return a HTTP 400 (BAD_REQUEST) if no id is provided"() {
//        when:
//        controller.doi()
//
//        then:
//        response.status == HttpStatus.SC_BAD_REQUEST
//    }
//
//    def "doi should return a HTTP 400 (BAD_REQUEST) if the id provided is not a UUID"() {
//        when:
//        params.id = "blabla"
//        controller.doi()
//
//        then:
//        response.status == HttpStatus.SC_BAD_REQUEST
//    }

    def "doi should return a HTTP 404 (NOT_FOUND) if the id provided does not match any DOI record"() {
        setup:
        controller.doiService.findByUuid(_) >> null

        when:
        params.id = UUID.randomUUID().toString()
        controller.doi()

        then:
        response.status == HttpStatus.SC_NOT_FOUND
    }

    def "doi should return a HTTP 200 (OK) and render the doi view if the id provided matches a DOI record"() {
        setup:
        Doi doi = new Doi(uuid: UUID.randomUUID().toString())
        controller.doiService.findByUuid(_) >> doi

        when:
        params.id = doi.uuid
        controller.doi()

        then:
        response.status == HttpStatus.SC_OK
        controller.modelAndView.viewName == "/doiResolve/doi"
        controller.modelAndView.model == [doi: doi]
    }

//    def "download should return a HTTP 400 (BAD_REQUEST) if no id is provided"() {
//        when:
//        controller.download()
//
//        then:
//        response.status == HttpStatus.SC_BAD_REQUEST
//    }

    def "download should search for DOI records by UUID"() {
        setup:
        String uuid = UUID.randomUUID().toString()
        when:
        params.id = uuid
        controller.download()

        then:
        1 * controller.doiService.findByUuid(uuid)
        0 * controller.doiService.findByDoi(_)
    }

    def "download should return the matching DOI's file"() {
        setup:
        Doi doi = new Doi(uuid: UUID.randomUUID().toString(), contentType: "text/plain", filename: "bla.txt")
        File dir = new File("${System.getProperty("java.io.tmpdir")}/${doi.uuid}")
        dir.mkdirs()
        File file = new File(dir, "bla.txt")
        file.createNewFile()
        file << "file content"

        when:
        params.id = doi.uuid
        controller.download()

        then:
        1 * controller.doiService.findByUuid(doi.uuid) >> doi
        1 * controller.fileService.getFileForDoi(_) >> file
        response.getHeader("Content-disposition") == 'attachment;filename=bla.txt'
        response.contentType == doi.contentType
        response.text == "file content"
    }
}
