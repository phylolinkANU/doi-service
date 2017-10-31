package au.org.ala.doi.util

class StateAssertions {

    private StateAssertions() {}

    /**
     * Throws an IllegalArgumentException if the provided argument is null or empty
     *
     * @param arg The argument to check
     */
    static void checkArgument(arg) {
        checkArgument(arg, "")
    }

    /**
     * Throws an IllegalArgumentException if the provided argument is null or empty
     *
     * @param arg The argument to check
     * @param an optional message to include in the IllegalArgumentException
     */
    static void checkArgument(arg, String message) {
        if (arg == null || (arg.getMetaClass() && arg.getMetaClass().respondsTo(arg, "isEmpty") && arg.isEmpty())) {
            throw new IllegalArgumentException(message)
        }
    }

    /**
     * Throws an IllegalStateException if the provided state evaluates to false with
     *
     * @param state The state to check
     */
    static void checkState(state) {
        checkState(state, "")
    }
    /**
     * Throws an IllegalStateException if the provided state evaluates to false with
     *
     * @param state The state to check
     * @param message an optional message to include in the IllegalArgumentException
     */
    static void checkState(state, String message) {
        if (!state) {
            throw new IllegalStateException(message)
        }
    }
}
