package au.org.ala.doi

import grails.testing.services.ServiceUnitTest
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

class FileServiceSpec extends Specification implements ServiceUnitTest<FileService> {

    def setup() {
        service.grailsApplication = [
                config: [
                        file: [
                                store: System.getProperty("java.io.tmpdir")
                        ]
                ]
        ]
    }

    def "getFileForDoi should throw an IllegalArgumentException if no doi is provided"() {
        when:
        service.getFileForDoi(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "getFileForDoi should return null if there is no file for the doi"() {
        when:
        File file = service.getFileForDoi(new Doi(filename: "bla.txt"))

        then:
        file == null
    }

    def "getFileForDoi should return the file when there is a file for the for"() {
        setup:
        Doi doi = new Doi(uuid: UUID.randomUUID(), filename: "testFile.txt")

        File dirs = new File("${System.getProperty("java.io.tmpdir")}/${doi.uuid}")
        dirs.mkdirs()
        File file = new File(dirs, "testFile.txt")
        file.createNewFile()

        expect:
        file.exists()

        when:
        File result = service.getFileForDoi(doi)

        then:
        result != null
    }

    def "storeFileForDoi should throw an IllegalArgumentException if no doi is provided"() {
        when:
        service.storeFileForDoi(null, Mock(MultipartFile))

        then:
        thrown(IllegalArgumentException)
    }

    def "storeFileForDoi should throw an IllegalArgumentException if no file is provided"() {
        when:
        service.storeFileForDoi(new Doi(), (MultipartFile) null)

        then:
        thrown(IllegalArgumentException)
    }

    def "storeFileForDoi should transfer the MultiPartFile to the DOI's directory in the file store"() {
        setup:
        Doi doi = new Doi(uuid: UUID.randomUUID())
        File expectedTarget = new File("${System.getProperty("java.io.tmpdir")}/${doi.uuid}/newFile.txt")

        MultipartFile file = Mock(MultipartFile)
        file.originalFilename >> "newFile.txt"

        when:
        service.storeFileForDoi(doi, file)

        then:
        1 * file.transferTo(expectedTarget)
    }

    def "storeFileForDoi should throw an IllegalArgumentException if no url is provided"() {
        when:
        service.storeFileForDoi(new Doi(), (String) null)

        then:
        thrown IllegalArgumentException
    }

    def "storeFileForDoi should download the file from the provided url into the DOI's directory in the file store"() {
        setup:
        Doi doi = new Doi(uuid: UUID.randomUUID())
        File expectedTarget = new File("${System.getProperty("java.io.tmpdir")}/${doi.uuid}/${doi.uuid}")

        when:
        service.storeFileForDoi(doi, "http://ala.org.au")

        then:
        expectedTarget.exists()
    }

    def "getDoiDirectory should throw an IllegalArgumentException if no doi is provided"() {
        when:
        service.getDoiDirectory(null)

        then:
        thrown IllegalArgumentException
    }

    def "getDoiDirectory should create the directory if it does not exist and create = true"() {
        when:
        File file = service.getDoiDirectory(new Doi(uuid: UUID.randomUUID()), true)

        then:
        file.exists()
    }

    def "getDoiDirectory should not create the directory if it does not exist and create = false"() {
        when:
        File file = service.getDoiDirectory(new Doi(uuid: UUID.randomUUID()))

        then:
        !file.exists()
    }
}
