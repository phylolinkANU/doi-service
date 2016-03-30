package au.ala.org.doi.ui

import au.org.ala.doi.Doi
import au.org.ala.doi.DoiService
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
        controller.modelAndView.model == [dois: [doi1, doi2], offset: 0, pageSize: 5]
    }

    def "doi should return a HTTP 400 (BAD_REQUEST) if no id is provided"() {
        when:
        controller.doi()

        then:
        response.status == HttpStatus.SC_BAD_REQUEST
    }

    def "doi should return a HTTP 400 (BAD_REQUEST) if the id provided is not a UUID"() {
        when:
        params.id = "blabla"
        controller.doi()

        then:
        response.status == HttpStatus.SC_BAD_REQUEST
    }

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
}
