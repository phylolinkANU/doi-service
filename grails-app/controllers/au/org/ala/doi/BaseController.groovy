package au.org.ala.doi

import grails.converters.JSON

import static org.apache.http.HttpStatus.SC_BAD_REQUEST
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR
import static org.apache.http.HttpStatus.SC_NOT_FOUND
import static org.apache.http.HttpStatus.SC_OK
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED

class BaseController {
    public static final String CONTEXT_TYPE_JSON = "application/json"

    def notFound = {String message = null ->
        sendError(SC_NOT_FOUND, message ?: "")
    }

    def badRequest = {String message = null ->
        sendError(SC_BAD_REQUEST, message ?: "")
    }

    def notAuthorised = {String message = null ->
        sendError(SC_UNAUTHORIZED, message ?: "You do not have permission to perform the requested action.")
    }

    def internalError = {String message = null ->
        sendError(SC_INTERNAL_SERVER_ERROR, message ?: "An unknown error occurred while processing your request")
    }

    def success = { resp ->
        response.status = SC_OK
        response.setContentType(CONTEXT_TYPE_JSON)
        render resp as JSON
    }

    def saveFailed = {
        sendError(SC_INTERNAL_SERVER_ERROR)
    }

    def sendError = {int status, String msg = null ->
        if (request.getHeader("accept")) {
            response.contentType = request.getHeader("accept").split(",")[0]
        }
        response.status = status
        response.sendError(status, msg)
    }

    def handle (resp) {
        if (resp) {
            if (resp.statusCode != SC_OK) {
                log.debug "Response status ${resp.statusCode} returned from operation"
                response.status = resp.statusCode
                sendError(resp.statusCode, resp.error ?: "")
            } else {
                response.setContentType(CONTEXT_TYPE_JSON)
                render resp.resp as JSON
            }
        } else {
            response.setContentType(CONTEXT_TYPE_JSON)
            render [:] as JSON
        }
    }
}
