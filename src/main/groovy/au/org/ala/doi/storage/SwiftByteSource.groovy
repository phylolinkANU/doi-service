package au.org.ala.doi.storage

import com.google.common.io.ByteSource
import org.javaswift.joss.model.StoredObject

class SwiftByteSource extends ByteSource {
    StoredObject obj

    @Override
    InputStream openStream() throws IOException {
        obj.downloadObjectAsInputStream()
    }
}
