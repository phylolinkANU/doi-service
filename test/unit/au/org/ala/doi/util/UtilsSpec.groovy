package au.org.ala.doi.util

import spock.lang.Specification

class UtilsSpec extends Specification {

    def "isUuid should return true for a valid UUID"() {
        when:
        boolean valid = Utils.isUuid(UUID.randomUUID().toString())

        then:
        valid
    }

    def "isUuid should return false for nulls and whitespace"() {
        when:
        boolean valid = Utils.isUuid(null)

        then:
        !valid

        when:
        valid = Utils.isUuid("")

        then:
        !valid

        when:
        valid = Utils.isUuid("      ")

        then:
        !valid
    }

    def "isUuid should return false for any non-UUID string"() {
        when:
        boolean valid = Utils.isUuid("not a uuid")

        then:
        !valid

        when:
        valid = Utils.isUuid(UUID.randomUUID().toString() + "2")

        then:
        !valid
    }
}
