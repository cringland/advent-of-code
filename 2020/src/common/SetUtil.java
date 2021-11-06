package common;

import java.util.HashSet;
import java.util.Set;

public final class SetUtil {

    public static <T> Set<T> difference(final Set<? extends T> s1, final Set<? extends T> s2) {
        Set<T> temp = new HashSet<T>(s1);
        temp.removeAll(s2);
        return temp;
    }

    public static <T> Set<T> symmetricDifference(final Set<? extends T> s1, final Set<? extends T> s2) {
        Set<T> symmetricDiff = new HashSet<T>(s1);
        symmetricDiff.addAll(s2);
        Set<T> tmp = new HashSet<T>(s1);
        tmp.retainAll(s2);
        symmetricDiff.removeAll(tmp);
        return symmetricDiff;
    }


    public static <T> Set<T> union(final Set<T> set1, final Set<T> set2) {
        var t = new HashSet<>(set1);
        t.addAll(set2);
        return t;
    }

    public static <T> Set<T> interesect(final Set<T> set1, final Set<T> set2) {
        var t = new HashSet<>(set1);
        t.retainAll(set2);
        return t;
    }
}
