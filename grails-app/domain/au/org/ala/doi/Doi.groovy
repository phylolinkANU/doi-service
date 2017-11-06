package au.org.ala.doi

import au.org.ala.doi.util.DoiProvider
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import net.kaleidos.hibernate.usertype.JsonbMapType

@ToString
@EqualsAndHashCode
class Doi {

    static final Set<String> ALLOWED_UPDATABLE_PROPERTIES = [
            'providerMetadata', 'customLandingPageUrl', 'title', 'authors', 'description', 'applicationUrl',
            'applicationMetadata'
    ].toSet()

    Long id

    UUID uuid
    String doi
    String title
    String authors
    String userId

    String licence
    String description
    Date dateMinted
    DoiProvider provider

    String filename
    String contentType
    byte[] fileHash
    Long fileSize

    Map providerMetadata
    Map applicationMetadata

    String customLandingPageUrl
    String applicationUrl

    Long version
    Date dateCreated
    Date lastUpdated

    static constraints = {
        applicationMetadata nullable: true
        customLandingPageUrl nullable: true, url: true
        applicationUrl nullable: true, url: true
        filename nullable: true
        contentType nullable: true
        licence nullable: true
        fileHash nullable: true
        fileSize nullable: true, min: 0l
        userId nullable: true
    }

    static mapping = {
        doi type: CitextType
        provider defaultValue: DoiProvider.ANDS
        providerMetadata type: JsonbMapType
        applicationMetadata type: JsonbMapType
    }

    def beforeValidate() {
        if (uuid == null) {
            uuid = UUID.randomUUID()
        }
    }
}
