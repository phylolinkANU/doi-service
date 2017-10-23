package au.org.ala.doi.util

class Utils {
    static final String UUID_REGEX = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/;

    static boolean isUuid(String value) {
        value =~ UUID_REGEX
    }
}
