package au.org.ala.doi

import au.org.ala.doi.util.DoiProvider
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.JsonbMapType

@ToString
@EqualsAndHashCode
class Doi {

    Long id

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

    Long version
    Date dateCreated
    Date lastUpdated

    static constraints = {
        applicationMetadata nullable: true
        customLandingPageUrl nullable: true
        applicationUrl nullable: true
    }

    static mapping = {
        uuid type: CitextType
        doi type: CitextType
        provider defaultValue: DoiProvider.ANDS
        providerMetadata type: JsonbMapType
        applicationMetadata type: JsonbMapType
    }

    def beforeValidate() {
        if (uuid == null) {
            uuid = UUID.randomUUID().toString()
        }
    }
}
