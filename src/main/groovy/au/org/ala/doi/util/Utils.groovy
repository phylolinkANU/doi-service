package au.org.ala.doi.util

import org.apache.commons.lang.StringUtils

import javax.mail.internet.ContentDisposition

class Utils {
    static final String UUID_REGEX = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/;

    static boolean isUuid(String value) {
        value =~ UUID_REGEX
    }

    static String extractFilename(String contentDisposition) {
        if (!contentDisposition) return ''
        def cd
        try {
            cd = new ContentDisposition(contentDisposition)
        } catch (e) {
            return ''
        }
        def pl = cd.parameterList
        def fn = pl.get('filename')
        return fn
    }

    static String extractLastPathSegment(String url) {
        return extractLastPathSegment(url.toURI())
    }

    static String extractLastPathSegment(URL url) {
        return StringUtils.substringAfterLast(url.path, '/')
    }

    static String extractLastPathSegment(URI uri) {
        return StringUtils.substringAfterLast(uri.path, '/')
    }
}
