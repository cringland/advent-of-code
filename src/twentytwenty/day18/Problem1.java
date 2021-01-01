package twentytwenty.day18;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Problem1 {


    //https://adventofcode.com/2020/day/18
    public static void main(String[] args) throws IOException {
        var sum = Files.readAllLines(Paths.get("src/twentytwenty/day18/input")).stream()
                .map(s -> s.replaceAll(" ", ""))
                .mapToLong(Problem1::solve)
                .peek(System.out::println)
                .sum();

        System.out.println("Problem 1 Answer is: " + sum);
    }

    private static Long solve(String s) {
        var copy = s;
        while (copy.contains("(")) {
            copy = removeABracket(copy);
        }
        while (copy.contains("(")) {
            copy = removeABracket(copy);
        }
        return solveNoBrack(copy);
    }


    private static Long solveNoBrack(final String input) {
        var matcher = Pattern.compile("[+*](?!.*[+*])").matcher(input);
        var num = getLastInt(input);
        if (matcher.find()) {
            var sign = matcher.group().charAt(0);
            var index = input.lastIndexOf(sign);
            return sign == '+' ? num + solveNoBrack(input.substring(0, index))
                    : num * solveNoBrack(input.substring(0, index));
        } else {
            return num;
        }
    }

    private static String removeABracket(String s) {
        int startIndex = 0;
        StringBuilder sb = new StringBuilder(s);
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch (ch) {
                case '(':
                    startIndex = i;
                    continue;
                case ')':
                    String bracketContents = s.substring(startIndex + 1, i);
                    Long solved = solveNoBrack(bracketContents);
                    return sb.replace(startIndex, i+1, solved.toString()).toString();
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

