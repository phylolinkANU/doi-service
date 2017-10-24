package au.org.ala.doi

import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(UUID) { uuid ->
            uuid.toString()
        }
    }
    def destroy = {
    }
}
