package au.org.ala.doi.util

import spock.lang.Specification

class StateAssertionsSpec extends Specification {

    StateAssertions stateAssertions = new Object() as StateAssertions

    def "checkArgument should throw an IllegalArgumentException for null or empty arguments"() {
        when:
        stateAssertions.checkArgument(value)

        then:
        thrown(IllegalArgumentException)

        where:
        value << [null, [], "", [:]]
    }

    def "checkState should not throw an IllegalArgumentException non-null or non-empty arguments or booleans"() {
        when:
        stateAssertions.checkArgument(value)

        then:
        noExceptionThrown()

        where:
        value << [new Object(), ["a","b"], "xyz", [a: "bla"], true, false, 1]
    }

    def "checkState should throw an IllegalStateException for null, empty arguments, or false"() {
        when:
        stateAssertions.checkState(value)

        then:
        thrown(IllegalStateException)

        where:
        value << [null, [], "", [:], false]
    }

    def "checkState should not throw an IllegalStateException non-null, non-empty arguments or true"() {
        when:
        stateAssertions.checkState(value)

        then:
        noExceptionThrown()

        where:
        value << [new Object(), ["a","b"], "xyz", [a: "bla"], true, 1]
    }
}
