package au.org.ala.doi

import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient

import static au.org.ala.doi.util.StateAssertions.*

class RestService {

    Map get(String url, ContentType contentType, ContentType requestContentType, Map headers = [:], Map query = [:] ) {
        RESTClient client = new RESTClient(url)

        HttpResponseDecorator response = client.get(
                headers: headers,
                query: query,
                requestContentType: requestContentType,
                contentType: contentType
        )

        [status: response.status, data: response.getData()]
    }

    Map post(String url, Map body, ContentType contentType, ContentType requestContentType, Map headers = [:], Map query = [:]) {
        RESTClient client = new RESTClient(url)

        HttpResponseDecorator response = client.post(headers: headers,
                query: query,
                requestContentType: requestContentType,
                contentType: contentType,
                body: body)

        [status: response.status, data: response.getData()]
    }
}
