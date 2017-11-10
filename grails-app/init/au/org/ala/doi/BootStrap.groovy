package au.org.ala.doi

import au.org.ala.doi.util.DoiProvider
import com.google.common.io.BaseEncoding
import grails.converters.JSON

class BootStrap {

    def init = { servletContext ->
        JSON.registerObjectMarshaller(UUID) { uuid ->
            uuid.toString()
        }
        JSON.registerObjectMarshaller(DoiProvider) { provider ->
            provider.toString()
        }
//        JSON.registerObjectMarshaller(byte[]) { bytes ->
//            BaseEncoding.base16().encode(bytes)
//        }
    }
    def destroy = {
    }
}
