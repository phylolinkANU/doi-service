package au.org.ala.doi.util

import org.apache.http.HttpStatus

class ServiceResponse {
    int httpStatus
    String doi
    String providerErrorCode
    String error

    ServiceResponse(String doi) {
        httpStatus = HttpStatus.SC_OK
        this.doi = doi
    }

    ServiceResponse(int httpStatus, String error, String providerErrorCode = null) {
        this.httpStatus = httpStatus
        this.error = error
        this.providerErrorCode = providerErrorCode
    }

    String getErrorMessage() {
        String message = "The service invocation returned HTTP ${httpStatus}"

        if (error) {
            message += " and error '${error}'"
        }
        if (providerErrorCode) {
            message += " with error code ${providerErrorCode}"
        }

        message
    }
}
