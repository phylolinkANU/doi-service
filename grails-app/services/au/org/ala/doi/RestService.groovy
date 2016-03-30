package au.org.ala.doi

import au.org.ala.doi.util.StateAssertions
import groovyx.net.http.ContentType
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient

class RestService implements StateAssertions {

    Map get(String url, ContentType contentType, ContentType requestContentType) {
        RESTClient client = new RESTClient(url)

        HttpResponseDecorator response = client.get(requestContentType: requestContentType, contentType: contentType)

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
