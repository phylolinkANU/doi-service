package au.org.ala.doi

import groovy.transform.Canonical

@Canonical
class MintResponse {
    String uuid
    String doi
    String error
    String landingPage
    String doiServiceLandingPage
    String status
}
