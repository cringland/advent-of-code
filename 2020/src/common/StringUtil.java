package common;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class StringUtil {

    public static String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }

    public static List<Long> allNums(String input) {
        var ints = new ArrayList<Long>();
        var matcher = Pattern.compile("\\d+").matcher(input);
        while (matcher.find())
            ints.add(Long.valueOf(matcher.group()));
        return ints;
    }

    public static List<String> allMatches(String regex, String input) {
        var strings = new ArrayList<String>();
        var matcher = Pattern.compile(regex).matcher(input);
        while (matcher.find())
            strings.add(matcher.group());
        return strings;
    }

    public static Long firstNum(String input) {
        return Long.valueOf(findFirst("\\d+", input));
    }

    public static String toString(List<Character> input) {
        return input.stream().map(Object::toString).collect(Collectors.joining());
    }

    public static List<Character> toList(String input) {
        var arr = input.chars().mapToObj(c -> (char) c).toArray(Character[]::new);
        return List.of(arr);
    }

    public static String findFirst(String regex, String input) {
        var matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find())
            return matcher.group();
        throw new IllegalStateException("Something went wrong for input: " + input);
    }
}
