package twentytwenty.day2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import common.StringUtil;
import common.Util;
import common.model.Range;

public class Problem {

    //https://adventofcode.com/2020/day/2
    public static void main(String[] args) throws IOException {
        var passwords = Files.readAllLines(Paths.get("src/twentytwenty/day2/input")).stream()
                .map(Password::new)
                .collect(Collectors.toList());

        var problem1 = passwords.stream()
                .filter(Password::p1Test)
                .count();

        long problem2 = passwords.stream()
                .filter(Password::p2Test)
                .count();

        System.out.println("Problem 1: Number of valid Passwords: " + problem1);
        Util.assertEquals(560L, problem1);
        System.out.println("Problem 2: Number of valid Passwords: " + problem2);
        Util.assertEquals(303L, problem2);
    }
}

class Password {

    private Range range;
    private String ch;
    private String password;

    public Password(String input) {
        var nums = StringUtil.allNums(input);
        range = Range.of(nums.get(0), nums.get(1));
        ch = StringUtil.findFirst("(.)(?=:)", input);
        password = StringUtil.findFirst("(\\w+)$", input);
    }

    public boolean p1Test() {
        long count = password.length() - password.replaceAll(ch, "").length();
        return range.testInclusive(count);
    }

    public boolean p2Test() {
        var pos1 = range.lowest().intValue() - 1;
        var pos2 = range.highest().intValue() - 1;
        return ch.equals(password.substring(pos1, pos1 + 1))
                ^ ch.equals(password.substring(pos2, pos2 + 1));
    }
}
