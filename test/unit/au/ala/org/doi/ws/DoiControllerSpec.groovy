package au.ala.org.doi.ws

import au.org.ala.doi.Doi
import au.org.ala.doi.DoiService
import au.org.ala.doi.FileService
import au.org.ala.doi.util.DoiProvider
import au.org.ala.doi.ws.DoiController
import grails.converters.JSON
import grails.test.mixin.TestFor
import org.apache.http.HttpStatus
import org.apache.http.entity.mime.FormBodyPart
import org.apache.http.entity.mime.content.StringBody
import org.codehaus.groovy.grails.plugins.testing.MockPart
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import spock.lang.Specification

import javax.servlet.http.Part

@TestFor(DoiController)
class DoiControllerSpec extends Specification {

    DoiController controller
    FileService fileService
    DoiService doiService

    def setup() {
        controller = new DoiController()
        fileService = Mock(FileService)
        controller.fileService = fileService
        doiService = Mock(DoiService)
        controller.doiService = doiService
    }

//    def "getDoi should return a HTTP 400 (BAD_REQUEST) if no id is provided"() {
//        when:
//        controller.getDoi()
//
//        then:
//        response.status == HttpStatus.SC_BAD_REQUEST
//    }

    def "getDoi should search for DOI records by UUID if the provided id is a UUID"() {
        setup:
        String uuid = UUID.randomUUID().toString()
        when:
        params.id = uuid
        controller.getDoi()

        then:
        1 * doiService.findByUuid(uuid)
        0 * doiService.findByDoi(_)
    }

    def "getDoi should search for DOI records by DOI if the provided id is not a UUID"() {
        setup:
        String id = "10.5072/63/56F35A9D3ECF7"
        when:
        params.id = id
        controller.getDoi()

        then:
        0 * doiService.findByUuid(_)
        1 * doiService.findByDoi(id)
    }

    def "getDoi should return a 404 (NOT_FOUND) if there was no matching DOI"() {
        setup:
        String id = "10.5072/63/56F35A9D3ECF7"
        when:
        params.id = id
        controller.getDoi()

        then:
        0 * doiService.findByUuid(_)
        1 * doiService.findByDoi(id) >> null
        response.status == HttpStatus.SC_NOT_FOUND
    }

    def "getDoi should return the matching DOI entity as JSON"() {
        setup:
        Doi doi = new Doi(uuid: UUID.randomUUID().toString())

        when:
        params.id = doi.uuid
        controller.getDoi()

        then:
        1 * doiService.findByUuid(doi.uuid) >> doi
        response.contentType == "application/json;charset=UTF-8"
    }

//    def "download should return a HTTP 400 (BAD_REQUEST) if no id is provided"() {
//        when:
//        controller.download()
//
//        then:
//        response.status == HttpStatus.SC_BAD_REQUEST
//    }

    def "download should search for DOI records by UUID if the provided id is a UUID"() {
        setup:
        String uuid = UUID.randomUUID().toString()
        when:
        params.id = uuid
        controller.download()

        then:
        1 * doiService.findByUuid(uuid)
        0 * doiService.findByDoi(_)
    }

    def "download should search for DOI records by DOI if the provided id is not a UUID"() {
        setup:
        String id = "10.5072/63/56F35A9D3ECF7"
        when:
        params.id = id
        controller.download()

        then:
        0 * doiService.findByUuid(_)
        1 * doiService.findByDoi(id)
    }

    def "download should return a 404 (NOT_FOUND) if there was no matching DOI"() {
        setup:
        String id = "10.5072/63/56F35A9D3ECF7"
        when:
        params.id = id
        controller.download()

        then:
        0 * doiService.findByUuid(_)
        1 * doiService.findByDoi(id) >> null
        response.status == HttpStatus.SC_NOT_FOUND
    }

    def "download should return a 404 (NOT_FOUND) if there was no file for the DOI"() {
        setup:
        String id = "10.5072/63/56F35A9D3ECF7"
        when:
        params.id = id
        controller.download()

        then:
        0 * doiService.findByUuid(_)
        1 * doiService.findByDoi(id) >> new Doi(uuid: "123")
        1 * fileService.getFileForDoi(_) >> null
        response.status == HttpStatus.SC_NOT_FOUND
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
        1 * doiService.findByUuid(doi.uuid) >> doi
        1 * fileService.getFileForDoi(_) >> file
        response.getHeader("Content-disposition") == 'attachment;filename=bla.txt'
        response.contentType == doi.contentType
        response.text == "file content"
    }

    def "mintDoi should return a 400 BAD_REQUEST if the request is NOT multipart and there is no fileUrl JSON property"() {
        when:
        request.JSON.provider = DoiProvider.ANDS.name()
        request.JSON.applicationUrl = "applicationUrl"
        request.JSON.providerMetadata = '{"foo": "bar"}'
        request.JSON.title = 'title'
        request.JSON.authors = 'authors'
        request.JSON.description = 'description'
        controller.mintDoi()

        then:
        response.status == HttpStatus.SC_BAD_REQUEST
    }

    def "mintDoi should return a 400 BAD_REQUEST if the provider is invalid"() {
        when:
        request.JSON.provider = "rubbish"
        request.JSON.applicationUrl = "applicationUrl"
        request.JSON.providerMetadata = '{"foo": "bar"}'
        request.JSON.title = 'title'
        request.JSON.authors = 'authors'
        request.JSON.description = 'description'
        request.JSON.fileUrl = "url"
        controller.mintDoi()

        then:
        response.status == HttpStatus.SC_BAD_REQUEST
    }

    def "mintDoi should invoke the DOI Service if provided all metadata and the fileUrl for non-multipart requests"() {
        when:
        request.JSON.provider = DoiProvider.ANDS.name()
        request.JSON.applicationUrl = "applicationUrl"
        request.JSON.providerMetadata = [foo: "bar"]
        request.JSON.title = 'title'
        request.JSON.authors = 'authors'
        request.JSON.description = 'description'
        request.JSON.fileUrl = "fileUrl"
        controller.mintDoi()

        then:
        1 * doiService.mintDoi(DoiProvider.ANDS, [foo: "bar"], "title", "authors", "description", "applicationUrl", "fileUrl", null, null, null) >> [:]
    }

    def "mintDoi should invoke the DOI Service if provided all metadata and a file in a multipart requests"() {
        setup:
        MultipartFile file = Mock(MultipartFile)

        when:
        Map data = [:]
        data.provider = DoiProvider.ANDS.name()
        data.applicationUrl = "applicationUrl"
        data.providerMetadata = [foo: "bar"]
        data.title = 'title'
        data.authors = 'authors'
        data.description = 'description'

        controller.request.addParameter("json", (data as JSON) as String)
        controller.request.addFile(file)
        controller.mintDoi()

        then:
        1 * doiService.mintDoi(DoiProvider.ANDS, [foo: "bar"], "title", "authors", "description", "applicationUrl", null, file, null, null) >> [:]
    }


}
