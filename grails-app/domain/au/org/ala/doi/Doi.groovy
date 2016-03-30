package au.org.ala.doi

import au.org.ala.doi.util.DoiProvider
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString
@EqualsAndHashCode
class Doi {

    String uuid
    String doi
    String title
    String authors
    String description
    Date dateMinted
    DoiProvider provider
    String filename
    String contentType

    Map providerMetadata
    Map applicationMetadata

    String customLandingPageUrl
    String applicationUrl

    static constraints = {
        applicationMetadata nullable: true
        customLandingPageUrl nullable: true
        applicationUrl nullable: true
    }

    static mapping = {
        provider defaultValue: DoiProvider.ANDS
    }

    def beforeValidate() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
        }
    }
}
