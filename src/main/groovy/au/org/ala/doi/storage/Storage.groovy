package au.org.ala.doi.storage

import au.org.ala.doi.Doi
import com.google.common.io.ByteSource
import org.springframework.web.multipart.MultipartFile

interface Storage {
    ByteSource getFileForDoi(Doi doi)

    void storeFileForDoi(Doi doi, MultipartFile incoming)

    void storeFileForDoi(Doi doi, String url)

    String getDescription()
}