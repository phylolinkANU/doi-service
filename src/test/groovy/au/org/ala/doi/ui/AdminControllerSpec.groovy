package au.org.ala.doi.ui

import au.org.ala.doi.DoiService
import au.org.ala.doi.storage.Storage
import au.org.ala.doi.util.DoiProvider
import au.org.ala.web.AuthService
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class AdminControllerSpec extends Specification implements ControllerUnitTest<AdminController> {

    Storage storage
    DoiService doiService
    AuthService authService


    def setup() {
        storage = Mock(Storage)
        //controller.storage = storage
        doiService = Mock(DoiService)
        authService = Mock(AuthService)
        controller.doiService = doiService
        controller.authService = authService
    }



    def "createDoi should invoke the DOI Service if provided all metadata and fileUrl for a new DOI"() {
        when:

        params.provider = DoiProvider.ANDS.name()
        params.applicationUrl = "applicationUrl"
        params.providerMetadata = "{'foo':'bar'}"
        params.title = 'title'
        params.authors = 'authors'
        params.description = 'description'
        params.fileUrl = "fileUrl"
        params.newExistingDoiRadio = "new"
        params.licence = 'licence'

        controller.createDoi()

        then:
        1 * doiService.mintDoi(DoiProvider.ANDS, [foo: "bar"], "title", "authors", "description", "licence", "applicationUrl", "fileUrl", null, null, null, null, null) >> [:]
    }

    def "createDoi should invoke the DOI Service if provided all metadata and fileUrl for an existing DOI"() {
        when:

        params.provider = DoiProvider.ANDS.name()
        params.applicationUrl = "applicationUrl"
        params.existingDoi = "A DOI"
        params.title = 'title'
        params.authors = 'authors'
        params.description = 'description'
        params.fileUrl = "fileUrl"
        params.newExistingDoiRadio = "existing"
        params.licence = 'licence'

        controller.createDoi()

        then:
        1 * doiService.mintDoi(DoiProvider.ANDS, null, "title", "authors", "description", "licence", "applicationUrl", "fileUrl", null, null, null, "A DOI", null) >> [:]
    }

    def "createDoi should add the current user id if linkToUser is set"() {
        when:

        params.provider = DoiProvider.ANDS.name()
        params.applicationUrl = "applicationUrl"
        params.existingDoi = "A DOI"
        params.title = 'title'
        params.authors = 'authors'
        params.description = 'description'
        params.fileUrl = "fileUrl"
        params.newExistingDoiRadio = "existing"
        params.licence = 'licence'
        params.linkToUser = 'true'

        controller.createDoi()

        then:
        1 * authService.userId >> '1'
        1 * doiService.mintDoi(DoiProvider.ANDS, null, "title", "authors", "description", "licence", "applicationUrl", "fileUrl", null, null, null, "A DOI", '1') >> [:]
    }

    def "createDoi does not invoike doiService if parameters are invalid"() {
        when:

        params.provider = null
        params.providerMetadata = "{'foo':'bar'}"
        params.applicationUrl = "applicationUrl"
        params.existingDoi = "A DOI"
        params.title = 'title'
        params.authors = 'authors'
        params.description = 'description'
        params.fileUrl = "fileUrl"
        params.newExistingDoiRadio = "existing"

        controller.createDoi()

        then:
        0 * doiService.mintDoi(_) >> [:]
        controller.modelAndView.viewName == "/admin/mintDoi"

        when:

        params.provider = DoiProvider.ANDS.name()
        params.providerMetadata = "{'foo':'bar'}"
        params.applicationUrl = null
        params.existingDoi = "A DOI"
        params.title = 'title'
        params.authors = 'authors'
        params.description = 'description'
        params.fileUrl = "fileUrl"
        params.newExistingDoiRadio = "existing"

        controller.createDoi()

        then:
        0 * doiService.mintDoi(_) >> [:]
        controller.modelAndView.viewName == "/admin/mintDoi"


        when: "No existing DOI is provided for an existing record"
        params.provider = DoiProvider.ANDS.name()
        params.providerMetadata = "{'foo':'bar'}"
        params.applicationUrl = "applicationUrl"
        params.existingDoi = null
        params.title = 'title'
        params.authors = 'authors'
        params.description = 'description'
        params.fileUrl = "fileUrl"
        params.newExistingDoiRadio = "existing"

        controller.createDoi()

        then:
        0 * doiService.mintDoi(_) >> [:]
        controller.modelAndView.viewName == "/admin/mintDoi"

        when: "No provider metadata is provided for a new DOI"
        params.provider = DoiProvider.ANDS.name()
        params.providerMetadata = null
        params.applicationUrl = "applicationUrl"
        params.existingDoi = "A DOI"
        params.title = 'title'
        params.authors = 'authors'
        params.description = 'description'
        params.fileUrl = "fileUrl"
        params.newExistingDoiRadio = "new"

        controller.createDoi()

        then:
        0 * doiService.mintDoi(_) >> [:]
        controller.modelAndView.viewName == "/admin/mintDoi"
    }

}
