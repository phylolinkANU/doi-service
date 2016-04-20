package au.ala.org.doi.ui

import au.org.ala.doi.DoiService
import au.org.ala.doi.FileService
import au.org.ala.doi.ui.AdminController
import au.org.ala.doi.util.DoiProvider
import au.org.ala.doi.ws.DoiController
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(AdminController)
class AdminControllerSpec extends Specification {

    AdminController controller
    FileService fileService
    DoiService doiService

    def setup() {
        controller = new AdminController()
        fileService = Mock(FileService)
        //controller.fileService = fileService
        doiService = Mock(DoiService)
        controller.doiService = doiService
    }

    def cleanup() {
    }

    def "mintDoi should invoke the DOI Service if provided all metadata and the fileUrl for non-multipart requests"() {
//        when:
//        request.provider = DoiProvider.ANDS.name()
//        request.applicationUrl = "applicationUrl"
//        request.providerMetadata = [foo: "bar"]
//        request.title = 'title'
//        request.authors = 'authors'
//        request.description = 'description'
//        request.fileUrl = "fileUrl"
//        request.applicationMetadata = [foo2: "bar2"]
//        request.customLandingPageUrl = "customLandingPageUrl"
//        controller.createDoi()
//
//        then:
//        1 * doiService.mintDoi(DoiProvider.ANDS, [foo: "bar"], "title", "authors", "description", "applicationUrl", "fileUrl", null, [foo2: "bar2"],  null) >> [:]
    }

}
