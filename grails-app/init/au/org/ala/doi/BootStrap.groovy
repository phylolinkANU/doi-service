package au.org.ala.doi

import au.org.ala.doi.util.DoiProvider
import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(UUID) { uuid ->
            uuid.toString()
        }
        JSON.registerObjectMarshaller(DoiProvider) { provider ->
            provider.toString()
        }
    }
    def destroy = {
    }
}
