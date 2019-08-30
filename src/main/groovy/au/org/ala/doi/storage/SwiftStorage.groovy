package au.org.ala.doi.storage

import au.org.ala.doi.Doi
import com.google.common.io.ByteSource
import org.javaswift.joss.instructions.UploadInstructions
import org.javaswift.joss.model.Account
import org.javaswift.joss.model.Container

class SwiftStorage extends BaseStorage {

    Account account
    String containerName
    private boolean privateStorage

    SwiftStorage(Account account, String container, boolean privateStorage) {
        this.account = account
        this.containerName = container
        this.privateStorage = privateStorage
    }

    private Container acquireContainer() {
        def container = account.getContainer(containerName)
        if (!container.exists()) {
            container = container.create()
        }
        if (privateStorage && container.isPublic()) {
            container.makePrivate()
        } else if (!privateStorage && !container.isPublic()) {
            container.makePrivate()
        }
        return container
    }

    @Override
    ByteSource getFileForDoi(Doi doi) {
        def key = keyForDoi(doi)
        def obj = acquireContainer().getObject(key)
        def exists = obj.exists()
        if (exists) {
            return new SwiftByteSource(obj: obj)
        }
        return null
    }

    @Override
    void transferInputStream(Doi doi, InputStream urlInputStream) {
        String key = keyForDoi(doi)

        def obj = acquireContainer().getObject(key)
        def upload = new UploadInstructions(urlInputStream)
        upload.contentType = doi.contentType
        obj.uploadObject(upload)
    }


    @Override
    String getDescription() {
        return "Swift Stack using $account, $containerName"
    }


    String keyForDoi(Doi doi) {
        "${doi.uuid}/${doi.filename}"
    }
}
