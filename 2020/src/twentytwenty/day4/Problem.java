package twentytwenty.day4;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/4
    private static final List<String> VALID_ECL = List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
    private static final Map<String, Predicate<String>> REQUIRED_FIELDS = Map.of(
            "byr", year(1920, 2002),
            "iyr", year(2010, 2020),
            "eyr", year(2020, 2030),
            "hgt", height(),
            "hcl", regex("^#[0-9a-f]{6}$"),
            "ecl", VALID_ECL::contains,
            "pid", regex("^\\d{9}$")
    );

    public static void main(String[] args) throws IOException {
        var withRequiredFields = Stream.of(Files.readString(Paths.get("src/twentytwenty/day4/input")).split("\n\n"))
                .map(s -> Stream.of(s.split("[ \\n]"))
                        .map(field -> field.split(":"))
                        .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1])))
                .filter(Problem::hasRequiredFields)
                .collect(Collectors.toList());

        var reqFieldCount = withRequiredFields.size();
        System.out.println("Problem 1: Valid passports = " + reqFieldCount);
        Util.assertEquals(190, reqFieldCount);

        var validPassportCount = withRequiredFields.stream().filter(Problem::isValid).count();
        System.out.println("Problem 2: Valid passports = " + validPassportCount);
        Util.assertEquals(121L, validPassportCount);
    }

    private static boolean hasRequiredFields(Map<String, String> passport) {
        return passport.keySet().containsAll(REQUIRED_FIELDS.keySet());
    }

    private static boolean isValid(Map<String, String> passport) {
        boolean containsReqKeys = passport.keySet().containsAll(REQUIRED_FIELDS.keySet());
        boolean allReqFieldsValid = passport.entrySet().stream()
                .allMatch((entry) -> !REQUIRED_FIELDS.containsKey(entry.getKey())
                        || REQUIRED_FIELDS.get(entry.getKey()).test(entry.getValue()));
        return containsReqKeys && allReqFieldsValid;
    }

    private static Predicate<String> year(int lower, int upper) {
        return s -> s.matches("^[0-9]{4}$") && Integer.valueOf(s) >= lower && Integer.valueOf(s) <= upper;
    }

    private static Predicate<String> regex(String pattern) {
        return s -> s.matches(pattern);
    }

    private static Predicate<String> height() {
        return s -> {
            if (s.matches("^[0-9]*cm$")) {
                int hgt = Integer.valueOf(s.replace("cm", ""));
                return hgt >= 150 && hgt <= 193;
            } else if (s.matches("^[0-9]*in$")) {
                int hgt = Integer.valueOf(s.replace("in", ""));
                return hgt >= 59 && hgt <= 76;
            }
            return false;
        };
    }


}
