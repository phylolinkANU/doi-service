package au.org.ala.doi.storage

import au.org.ala.doi.Doi
import com.google.common.io.ByteSource
import org.springframework.web.multipart.MultipartFile
import spock.lang.Specification

class FileStorageSpec extends Specification {

    FileStorage fileStorage

    def setup() {
        fileStorage = new FileStorage(System.getProperty("java.io.tmpdir"))
    }

    def "getFileForDoi should throw an IllegalArgumentException if no doi is provided"() {
        when:
        fileStorage.getFileForDoi(null)

        then:
        thrown(IllegalArgumentException)
    }

    def "getFileForDoi should return null if there is no file for the doi"() {
        when:
        ByteSource file = fileStorage.getFileForDoi(new Doi(filename: "bla.txt"))

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
        ByteSource result = fileStorage.getFileForDoi(doi)

        then:
        result != null
    }

    def "storeFileForDoi should throw an IllegalArgumentException if no doi is provided"() {
        when:
        fileStorage.storeFileForDoi(null, Mock(MultipartFile))

        then:
        thrown(IllegalArgumentException)
    }

    def "storeFileForDoi should throw an IllegalArgumentException if no file is provided"() {
        when:
        fileStorage.storeFileForDoi(new Doi(), (MultipartFile) null)

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
        fileStorage.storeFileForDoi(doi, file)

        then:
        1 * file.transferTo(expectedTarget)
    }

    def "storeFileForDoi should throw an IllegalArgumentException if no url is provided"() {
        when:
        fileStorage.storeFileForDoi(new Doi(), (String) null)

        then:
        thrown IllegalArgumentException
    }

    def "storeFileForDoi should download the file from the provided url into the DOI's directory in the file store"() {
        setup:
        Doi doi = new Doi(uuid: UUID.randomUUID())
        File expectedTarget = new File("${System.getProperty("java.io.tmpdir")}/${doi.uuid}/${doi.uuid}")

        when:
        fileStorage.storeFileForDoi(doi, "http://ala.org.au")

        then:
        expectedTarget.exists()
    }

    def "getDoiDirectory should throw an IllegalArgumentException if no doi is provided"() {
        when:
        fileStorage.getDoiDirectory(null)

        then:
        thrown IllegalArgumentException
    }

    def "getDoiDirectory should create the directory if it does not exist and create = true"() {
        when:
        File file = fileStorage.getDoiDirectory(new Doi(uuid: UUID.randomUUID()), true)

        then:
        file.exists()
    }

    def "getDoiDirectory should not create the directory if it does not exist and create = false"() {
        when:
        File file = fileStorage.getDoiDirectory(new Doi(uuid: UUID.randomUUID()))

        then:
        !file.exists()
    }
}
