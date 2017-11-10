package au.org.ala.doi

import au.org.ala.doi.util.DoiProvider
import groovy.transform.Canonical

@Canonical
class MintRequest {

    DoiProvider provider
    Map providerMetadata
    String title
    String authors
    String description
    String licence
    String applicationUrl
    String fileUrl
    String applicationMetadata
    String customLandingPageUrl
    String userId

}
