package au.ala.org.doi.providers

import au.org.ala.doi.exceptions.DoiMintingException
import au.org.ala.doi.providers.DoiProviderService
import au.org.ala.doi.util.ServiceResponse
import org.apache.http.HttpStatus
import spock.lang.Specification

class DoiProviderServiceSpec extends Specification {

    def "generateLandingPageUrl should return the customLandingPageUrl if provided"() {
        given:
        DoiProviderService service = [
                generateRequestPayload: { metadata, landingPage -> "payload"},
                invokeService: {}
        ] as DoiProviderService

        when:
        String url = service.generateLandingPageUrl("uuid", "customLandingPage")

        then:
        url == "customLandingPage"
    }

    def "generateLandingPageUrl should return a standard, constructed URL when there is no customLandingPageUrl"() {
        given:
        DoiProviderService service = [
                generateRequestPayload: { metadata, landingPage -> "payload"},
                invokeService: {},
                getGenericLandingPageUrlPrefix: {"serverUrl"}
        ] as DoiProviderService

        when:
        String url = service.generateLandingPageUrl("uuid1")

        then:
        url == "serverUrl/doi/uuid1"
    }

    def "mintDoi should throw an IllegalArgumentException if no uuid was provided"() {
        given:
        DoiProviderService service = [
                generateRequestPayload: { throw new Exception("test")},
                invokeService: {},
                getGenericLandingPageUrlPrefix: {"serverUrl"}
        ] as DoiProviderService

        when:
        service.mintDoi(null, [foo: "bar"])

        then:
        thrown IllegalArgumentException
    }

    def "mintDoi should throw an IllegalArgumentException if no metadata was provided"() {
        given:
        DoiProviderService service = [
                generateRequestPayload: { throw new Exception("test")},
                invokeService: {},
                getGenericLandingPageUrlPrefix: {"serverUrl"}
        ] as DoiProviderService

        when:
        service.mintDoi("uuid", [:])

        then:
        thrown IllegalArgumentException
    }

    def "mintDoi should throw a DoiMintingException if generateRequestPayload results in an exception"() {
        given:
        DoiProviderService service = [
                generateRequestPayload: { throw new Exception("test")},
                invokeService: {},
                getGenericLandingPageUrlPrefix: {"serverUrl"}
        ] as DoiProviderService

        when:
        service.mintDoi("uuid", [foo: "bar"])

        then:
        thrown DoiMintingException
    }

    def "mintDoi should throw a DoiMintingException if invokeService results in an exception"() {
        given:
        DoiProviderService service = [
                generateRequestPayload: { metadata, landingPage -> "payload"},
                invokeService: { throw new Exception("test")},
                getGenericLandingPageUrlPrefix: {"serverUrl"}
        ] as DoiProviderService

        when:
        service.mintDoi("uuid", [foo: "bar"])

        then:
        thrown DoiMintingException
    }

    def "mintDoi should throw a DoiMintingException if invokeService returns a ServiceResponse with a result code other than HTTP 200"() {
        given:
        DoiProviderService service = [
                generateRequestPayload: { metadata, landingPage -> "payload"},
                invokeService: { payload, landingPage -> new ServiceResponse(HttpStatus.SC_BAD_REQUEST, "bad")},
                getGenericLandingPageUrlPrefix: {"serverUrl"}
        ] as DoiProviderService

        when:
        service.mintDoi("uuid", [foo: "bar"])

        then:
        thrown DoiMintingException
    }

    def "mintDoi should throw a DoiMintingException if invokeService returns a ServiceResponse with a result code of HTTP 200 but not DOI"() {
        given:
        DoiProviderService service = [
                generateRequestPayload: { metadata, landingPage -> "payload"},
                invokeService: { payload, landingPage -> new ServiceResponse(HttpStatus.SC_OK, "bad")},
                getGenericLandingPageUrlPrefix: {"serverUrl"}
        ] as DoiProviderService

        when:
        service.mintDoi("uuid", [foo: "bar"])

        then:
        thrown DoiMintingException
    }

    def "mintDoi should return the new DOI on success"() {
        given:
        DoiProviderService service = [
                generateRequestPayload: { metadata, landingPage -> "payload"},
                invokeService: { payload, landingPage -> new ServiceResponse("newDoi")},
                getGenericLandingPageUrlPrefix: {"serverUrl"}
        ] as DoiProviderService

        when:
        String doi = service.mintDoi("uuid", [foo: "bar"])

        then:
        doi == "newDoi"
    }
}
