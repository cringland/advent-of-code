package common;

import static java.lang.String.format;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import common.model.Range;

public final class Util {

    public static void assertEquals(final Object expected, final Object actual) {
        if (expected != actual && !expected.equals(actual)) {
            throw new AssertionError(format("Assertion failed! Expected \"%s\" but was \"%s\"", expected.toString(), actual.toString()));
        }
    }

    public static Long sum(Collection<Long> list) {
        if (list.size() >= 2)
            return list.stream().reduce(Math::addExact).get();
        else {
            var hasElement = list.size() == 1;
            return hasElement ? list.iterator().next() : 0L;
        }
    }

    public static Range range(Collection<Long> list) {
        List<Long> sorted = list.stream().sorted().collect(Collectors.toList());
        long smallest = sorted.get(0);
        long largest = sorted.get(sorted.size() - 1);
        return Range.of(smallest, largest);
    }
}
