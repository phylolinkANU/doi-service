package au.org.ala.doi

import groovy.transform.Canonical

@Canonical
class UpdateRequest {
    /*
    'providerMetadata', 'customLandingPageUrl', 'title', 'authors', 'description', 'applicationUrl',
            'applicationMetadata'
     */

    Map providerMetadata
    String customLandingPageUrl
    String title
    String authors
    String description
    String applicationUrl
    Map applicationMetadata

    String fileUrl
}
