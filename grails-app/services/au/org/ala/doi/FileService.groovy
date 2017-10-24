package au.org.ala.doi

import au.org.ala.doi.util.StateAssertions
import org.springframework.web.multipart.MultipartFile

class FileService implements StateAssertions {

    def grailsApplication

    File getFileForDoi(Doi doi) {
        checkArgument doi

        File file = new File(getDoiDirectory(doi), doi.filename)

        if (!file.exists()) {
            file = null
        }

        file
    }

    void storeFileForDoi(Doi doi, MultipartFile incoming) {
        checkArgument doi
        checkArgument incoming

        incoming.transferTo(new File(getDoiDirectory(doi, true), incoming.originalFilename))
    }

    void storeFileForDoi(Doi doi, String url) {
        checkArgument doi
        checkArgument url

        File file = new File(getDoiDirectory(doi, true), doi.filename ?: doi.uuid.toString())
        file.withOutputStream { output ->
            URL urlObject = new URL(url)
            urlObject.withInputStream { input ->
                output << input
            }
            doi.contentType = urlObject.openConnection().contentType
        }
    }

    File getDoiDirectory(Doi doi, boolean create = false) {
        checkArgument doi

        File dir = new File("${grailsApplication.config.file.store}/${doi.uuid.toString()}")

        if (!dir.exists() && create) {
            dir.mkdirs()
        }

        dir
    }
}
