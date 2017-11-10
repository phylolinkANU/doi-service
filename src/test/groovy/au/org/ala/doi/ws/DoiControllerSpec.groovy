package au.org.ala.doi.ws

import au.org.ala.doi.Doi
import au.org.ala.doi.DoiService
import au.org.ala.doi.MintResponse
import au.org.ala.doi.storage.Storage
import au.org.ala.doi.util.DoiProvider
import com.google.common.io.ByteSource
import com.google.common.io.Files
import grails.converters.JSON
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import org.apache.http.HttpStatus
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

class DoiControllerSpec extends Specification implements ControllerUnitTest<DoiController>, DataTest {

    Storage storage
    DoiService doiService

    def setupSpec() {
        mockDomain Doi
    }

    def setup() {
        storage = Mock(Storage)
        controller.storage = storage
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

    def "show should search for DOI records by UUID if the provided id is a UUID"() {
        setup:
        String uuid = UUID.randomUUID().toString()
        when:
        params.id = uuid
        controller.show()

        then:
        1 * doiService.findByUuid(uuid)
        0 * doiService.findByDoi(_)
    }

    def "show should search for DOI records by DOI if the provided id is not a UUID"() {
        setup:
        String id = "10.5072/63/56F35A9D3ECF7"
        when:
        params.id = id
        controller.show()

        then:
        0 * doiService.findByUuid(_)
        1 * doiService.findByDoi(id)
    }

    def "show should return a 404 (NOT_FOUND) if there was no matching DOI"() {
        setup:
        String id = "10.5072/63/56F35A9D3ECF7"
        when:
        params.id = id
        controller.show()

        then:
        0 * doiService.findByUuid(_)
        1 * doiService.findByDoi(id) >> null
        response.status == HttpStatus.SC_NOT_FOUND
    }

    def "show should return the matching DOI entity as JSON"() {
        setup:
        Doi doi = new Doi(uuid: UUID.randomUUID())

        when:
        params.id = doi.uuid.toString()
        controller.show()

        then:
        1 * doiService.findByUuid(doi.uuid.toString()) >> doi
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
        1 * doiService.findByDoi(id) >> new Doi(uuid: UUID.randomUUID())
        1 * storage.getFileForDoi(_) >> null
        response.status == HttpStatus.SC_NOT_FOUND
    }

    def "download should return the matching DOI's file"() {
        setup:
        Doi doi = new Doi(uuid: UUID.randomUUID(), contentType: "text/plain", filename: "bla.txt")
        File dir = new File("${System.getProperty("java.io.tmpdir")}/${doi.uuid}")
        dir.mkdirs()
        File file = new File(dir, "bla.txt")
        file.createNewFile()
        file << "file content"
        ByteSource bs = Files.asByteSource(file)

        when:
        params.id = doi.uuid.toString()
        controller.download()

        then:
        1 * doiService.findByUuid(doi.uuid.toString()) >> doi
        1 * storage.getFileForDoi(_) >> bs
        response.getHeader("Content-disposition") == 'attachment;filename=bla.txt'
        response.contentType == doi.contentType
        response.text == "file content"
    }

    def "save should return a 400 BAD_REQUEST if the provider is invalid"() {
        when:
        request.JSON.provider = "rubbish"
        request.JSON.applicationUrl = "http://example.org/applicationUrl"
        request.JSON.providerMetadata = '{"foo": "bar"}'
        request.JSON.title = 'title'
        request.JSON.authors = 'authors'
        request.JSON.description = 'description'
        request.JSON.fileUrl = "url"
        controller.save()

        then:
        response.status == HttpStatus.SC_BAD_REQUEST
    }

    def "save should invoke the DOI Service if provided all metadata and the fileUrl for non-multipart requests"() {
        when:
        request.JSON.provider = DoiProvider.ANDS.name()
        request.JSON.applicationUrl = "http://example.org/applicationUrl"
        request.JSON.providerMetadata = [foo: "bar"]
        request.JSON.title = 'title'
        request.JSON.authors = 'authors'
        request.JSON.description = 'description'
        request.JSON.fileUrl = "fileUrl"
        request.JSON.userId = '1'
        controller.save()

        then:
        1 * doiService.mintDoi(DoiProvider.ANDS, [foo: "bar"], "title", "authors", "description", null, "http://example.org/applicationUrl", "fileUrl", null, null, null, null, '1') >> new MintResponse()
    }

    def "save should invoke the DOI Service if provided all metadata and a file in a multipart requests"() {
        setup:
        MultipartFile file = Mock(MultipartFile)

        when:
        Map data = [:]
        data.provider = DoiProvider.ANDS.name()
        data.applicationUrl = "http://example.org/applicationUrl"
        data.providerMetadata = [foo: "bar"]
        data.title = 'title'
        data.authors = 'authors'
        data.description = 'description'
        data.licence = 'licence'

        controller.request.addParameter("json", (data as JSON) as String)
        controller.request.addFile(file)
        controller.save()

        then:
        1 * doiService.mintDoi(DoiProvider.ANDS, [foo: "bar"], "title", "authors", "description", "licence", "http://example.org/applicationUrl", null, file, null, null, null, null) >> new MintResponse()
    }


}
