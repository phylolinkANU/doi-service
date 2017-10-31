package au.org.ala.doi.storage

import au.org.ala.doi.Doi
import com.google.common.io.ByteSource
import com.google.common.io.Files
import groovy.util.logging.Slf4j
import org.springframework.web.multipart.MultipartFile

import static au.org.ala.doi.util.StateAssertions.*

@Slf4j
class FileStorage extends BaseStorage {

    String fileStoreLocation

    FileStorage(String fileStoreLocation) {
        this.fileStoreLocation = fileStoreLocation
        init()
    }

    void init() {
        checkArgument fileStoreLocation
        def dir = new File(fileStoreLocation)
        if (!dir.isDirectory()) {
            log.warn("$fileStoreLocation is not a directory")
        } else if (!dir.canWrite()) {
            log.warn("$fileStoreLocation is not writable")
        }
    }

    @Override
    ByteSource getFileForDoi(Doi doi) {
        checkArgument doi

        File file = new File(getDoiDirectory(doi), doi.filename)

        return file.exists() ? Files.asByteSource(file) : null
    }

    @Override
    void storeFileForDoi(Doi doi, MultipartFile incoming) {
        checkArgument doi
        checkArgument incoming

        incoming.transferTo(new File(getDoiDirectory(doi, true), incoming.originalFilename))
    }

    @Override
    void transferInputStream(Doi doi, InputStream urlInputStream) {
        File file = new File(getDoiDirectory(doi, true), doi.filename ?: doi.uuid.toString())
        file.withOutputStream { output ->
            output << urlInputStream
        }
    }

    @Override
    String getDescription() {
        return "local filesystem at path $fileStoreLocation"
    }

    File getDoiDirectory(Doi doi, boolean create = false) {
        checkArgument doi

        File dir = new File("$fileStoreLocation/${doi.uuid.toString()}")

        if (!dir.exists() && create) {
            dir.mkdirs()
        }

        dir
    }

}
