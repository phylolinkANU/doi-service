package au.org.ala.doi.storage

import com.amazonaws.services.s3.AmazonS3Client
import com.google.common.io.ByteSource

class S3ByteSource extends ByteSource {

    String bucket
    String key
    AmazonS3Client client

    @Override
    InputStream openStream() throws IOException {
        return client.getObject(bucket, key).getObjectContent()
    }
}