package common;

import static java.lang.String.format;

public final class Util {

    public static void assertEquals(final Object expected, final Object actual) {
        if (expected != actual && !expected.equals(actual)) {
            throw new AssertionError(format("Assertion failed! Expected \"%s\" but was \"%s\"", expected.toString(), actual.toString()));
        }
    }
}
