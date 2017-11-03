package au.org.ala.doi.storage

import au.org.ala.doi.Doi
import au.org.ala.doi.util.Utils
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import groovy.transform.stc.SecondParam
import org.springframework.web.multipart.MultipartFile

import static au.org.ala.doi.util.StateAssertions.checkArgument
import static grails.web.http.HttpHeaders.CONTENT_DISPOSITION

abstract class BaseStorage implements Storage {

    static final String DEFAULT_CONTENT_TYPE = 'application/octet-stream'

    @Override
    void storeFileForDoi(Doi doi, MultipartFile mpf) {
        storeMultipartFile(doi, mpf) { transferInputStream(doi, mpf.inputStream) }
    }

    protected void storeMultipartFile(Doi doi, MultipartFile mpf, Closure<Void> transfer) {
        checkArgument doi
        checkArgument mpf

        String filename = doi.filename ?: mpf.originalFilename ?: doi.uuid.toString()
        doi.filename = filename
        doi.contentType = mpf.contentType ?: DEFAULT_CONTENT_TYPE

        transfer()
    }

    @Override
    void storeFileForDoi(Doi doi, String url) {
        checkArgument doi
        checkArgument url

        URL urlObject = new URL(url)
        def connection = urlObject.openConnection()
        def contentType = connection.contentType
        String filename = Utils.extractFilename(connection.getHeaderField(CONTENT_DISPOSITION)) ?: Utils.extractLastPathSegment(urlObject) ?: doi.uuid.toString()
        doi.contentType = contentType ?: DEFAULT_CONTENT_TYPE
        doi.filename = filename

        urlObject.withInputStream { input ->
            transferInputStream(doi, input)
        }

    }

    abstract void transferInputStream(Doi doi, InputStream urlInputStream)
}
