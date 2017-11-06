package au.org.ala.doi.storage

import au.org.ala.doi.Doi
import au.org.ala.doi.util.Utils
import com.google.common.hash.Hashing
import com.google.common.hash.HashingInputStream
import com.google.common.io.ByteStreams
import com.google.common.io.CountingInputStream
import org.springframework.web.multipart.MultipartFile

import static au.org.ala.doi.util.StateAssertions.checkArgument
import static grails.web.http.HttpHeaders.CONTENT_DISPOSITION

abstract class BaseStorage implements Storage {

    static final String DEFAULT_CONTENT_TYPE = 'application/octet-stream'

    @Override
    void storeFileForDoi(Doi doi, MultipartFile mpf) {
        storeMultipartFile(doi, mpf, this.&hashAndCountInputStream.curry(doi, mpf.inputStream))
    }

    protected void storeMultipartFile(Doi doi, MultipartFile mpf, Closure<Void> transfer) {
        checkArgument doi
        checkArgument mpf

        String filename = doi.filename ?: mpf.originalFilename ?: doi.uuid.toString()
        doi.filename = filename
        doi.contentType = mpf.contentType ?: DEFAULT_CONTENT_TYPE
        doi.fileSize = mpf.size
        // This is kind of a hack that relies on the MultipartFile implementation always buffering
        // the input to memory or to disk and returning a new input stream each time getInputStream is called.
        doi.fileHash = new HashingInputStream(Hashing.sha256(), mpf.inputStream).withStream { is -> ByteStreams.exhaust(is); is.hash().asBytes() }

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

        hashAndCountInputStream(doi, urlObject.newInputStream())
    }

    protected void hashAndCountInputStream(Doi doi, InputStream inputStream) {
        def countingInput = new CountingInputStream(inputStream)
        def hashingInput = new HashingInputStream(Hashing.sha256(), countingInput)

        hashingInput.withStream { input ->
            transferInputStream(doi, input)
        }

        doi.fileHash = hashingInput.hash().asBytes()
        doi.fileSize = countingInput.count
    }

    abstract void transferInputStream(Doi doi, InputStream urlInputStream)
}
