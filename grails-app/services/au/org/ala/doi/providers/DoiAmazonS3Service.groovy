package au.org.ala.doi.providers

import com.amazonaws.services.s3.S3ClientOptions
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import grails.artefact.Artefact
import grails.plugin.awssdk.s3.AmazonS3Service
import reactor.bus.Bus

/**
 * Overrides the AWS plugin's S3 Service to allow path style access and a custom endpoint to be used.
 */
@Artefact('Service')
class DoiAmazonS3Service extends AmazonS3Service {

    String defaultBucketName

    @Override
    void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet()
        if (endpoint) {
            this.client.setEndpoint(endpoint)
            this.client.s3ClientOptions = S3ClientOptions.builder().setPathStyleAccess(true).build()
        }
        this.defaultBucketName = serviceConfig?.bucket ?: ''
    }

    // To make groovy happy
    Bus sendAndReceive(Object key, Closure reply) {
        super.sendAndReceive(key, reply)
    }

    String getEndpoint() {
        serviceConfig.getProperty('endpoint')
    }

    @Override
    String storeInputStream(String bucketName,
                            String path,
                            InputStream input,
                            ObjectMetadata metadata) {
        def result = super.storeInputStream(bucketName, path, input, metadata)
        if (!result) return result
        if (endpoint) {
            def endpointResult = "$endpoint/$bucketName/$path"
            log.info("Overriding result {} with endpoint {}", result, endpointResult)
            return endpointResult
        } else {
            return result
        }
    }

    @Override
    String storeFile(String bucketName,
                     String path,
                     File file,
                     CannedAccessControlList cannedAcl = CannedAccessControlList.PublicRead) {
        def result = super.storeFile(bucketName, path, file, cannedAcl)
        if (!result) return result
        if (endpoint) {
            def endpointResult = "$endpoint/$bucketName/$path"
            log.info("Overriding result {} with endpoint {}", result, endpointResult)
            return endpointResult
        } else {
            return result
        }
    }
}
