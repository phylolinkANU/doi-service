package au.org.ala.doi.storage

import au.org.ala.doi.Doi
import au.org.ala.doi.util.Utils

import static au.org.ala.doi.util.StateAssertions.checkArgument
import static grails.web.http.HttpHeaders.CONTENT_DISPOSITION

abstract class BaseStorage implements Storage {
    @Override
    void storeFileForDoi(Doi doi, String url) {
        checkArgument doi
        checkArgument url

        URL urlObject = new URL(url)
        def connection = urlObject.openConnection()
        def contentType = connection.contentType
        def filename = Utils.extractFilename(connection.getHeaderField(CONTENT_DISPOSITION)) ?: Utils.extractLastPathSegment(urlObject) ?: doi.uuid
        doi.contentType = contentType
        doi.filename = filename

        urlObject.withInputStream { input ->
            transferInputStream(doi, input)
        }

    }

    abstract void transferInputStream(Doi doi, InputStream urlInputStream)
}
