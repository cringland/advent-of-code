package day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import common.Util;

import static common.StringUtil.allNums;
import static common.Util.sum;

public class Problem {

    //https://adventofcode.com/2020/day/18
    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Paths.get("src/twentytwenty/day18/input")).stream()
                .map(s -> s.replaceAll(" ", ""))
                .collect(Collectors.toList());

        var sumP1 = solveAll(input, Problem::solveSimple);
        System.out.println("Problem 1 Answer is: " + sumP1);
        Util.assertEquals(24650385570008L, sumP1);

        var sumP2 = solveAll(input, Problem::solveAdvanced);
        System.out.println("Problem 2 Answer is: " + sumP2);
        Util.assertEquals(158183007916215L, sumP2);
    }

    private static long solveAll(final List<String> input, final Function<String, Long> solver) {
        return input.stream()
                .mapToLong(i -> solve(i, solver))
                .sum();
    }

    //solver.apply("8*9*4*60+4+6")
    private static Long solve(String s, final Function<String, Long> solver) {
        var copy = s;
        while (copy.contains("(")) {
            copy = removeABracket(copy, solver);
        }
        return solver.apply(copy);
    }

    private static Long solveAdvanced(final String org) {
        String input = org;
        while (input.contains("+")) {
            var matcher = Pattern.compile("(\\d*)(?:\\++)(?!.*\\+)(\\d*)").matcher(input);
            matcher.find();
            var sum = matcher.group();
            var nums = allNums(sum);
            input = input.replaceFirst("(\\d*)(?:\\++)(?!.*\\+)(\\d*)", sum(nums).toString());
        }
        var matcher = Pattern.compile("[+*](?!.*[+*])").matcher(input);
        var num = getLastInt(input);
        if (matcher.find()) {
            var sign = matcher.group().charAt(0);
            var index = input.lastIndexOf(sign);
            return sign == '+' ? num + solveAdvanced(input.substring(0, index))
                    : num * solveAdvanced(input.substring(0, index));
        } else {
            return num;
        }
    }

    private static Long solveSimple(final String input) {
        var matcher = Pattern.compile("[+*](?!.*[+*])").matcher(input);
        var num = getLastInt(input);
        if (matcher.find()) {
            var sign = matcher.group().charAt(0);
            var index = input.lastIndexOf(sign);
            return sign == '+' ? num + solveSimple(input.substring(0, index))
                    : num * solveSimple(input.substring(0, index));
        } else {
            return num;
        }
    }

    private static String removeABracket(String s, final Function<String, Long> solver) {
        int startIndex = 0;
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '(':
                    startIndex = i;
                    continue;
                case ')':
                    //solver.apply("8*9*4+60+4+6")
                    String bracketContents = s.substring(startIndex + 1, i);
                    Long solved = solver.apply(bracketContents);
                    return sb.replace(startIndex, i + 1, solved.toString()).toString();
            }
        }
        return "";
    }

    private static Long getLastInt(String input) {
        var matcher = Pattern.compile("(\\d+)(?!.*\\d)").matcher(input);
        if (matcher.find())
            return Long.valueOf(matcher.group());
        throw new IllegalStateException("Something went wrong for input: " + input);
    }
}



