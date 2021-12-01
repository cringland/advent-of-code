package twentytwenty.day19;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

import common.Util;

import static common.StringUtil.allNums;

public class Problem {

    //https://adventofcode.com/2020/day/19
    public static void main(String[] args) throws IOException {
        var input = Files.readString(Paths.get("src/twentytwenty/day19/input")).split("\n\n");
        Map<Integer, String> rules = input[0].lines()
                .map(str -> {
                    var split = str.split(":");
                    return new AbstractMap.SimpleEntry<>(Integer.valueOf(split[0]), split[1]);
                })
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String pattern1 = getPattern(rules);
        var p1Ans = input[1].lines().filter(s -> s.matches(pattern1)).count();
        System.out.println("Problem 1 Answer is: " + p1Ans);
        Util.assertEquals(241L, p1Ans);

        String pattern2 = getPatternP2(rules);
        var p2Ans = input[1].lines().filter(s -> s.matches(pattern2)).count();
        System.out.println("Problem 2 Answer is: " + p2Ans);
        Util.assertEquals(424L, p2Ans);
    }

    private static String getPatternP2(final Map<Integer, String> rules) {
        String r42 = getPattern(42, rules);
        String r31 = getPattern(31, rules);

        //Should really revisit this as I stole it
        String masterRegex = "^((42+) ((42 31) | (42{2} 31{2}) | (42{3} 31{3}) | (42{4} 31{4}) | (42{5} 31{5}) | (42{6} 31{6}) | (42{7} 31{7}) | (42{8} 31{8}) | (42{9} 31{9}) | (42{10} 31{10})))$";
        return masterRegex.replace("42", r42).replace("31", r31).replace(" ", "");
    }

    private static String getPattern(Map<Integer, String> rules) {
        return "^" + getPattern(0, rules) + "$";
    }

    private static String getPattern(int idx, Map<Integer, String> rules) {
        final String currentRule = rules.get(idx);
        if (currentRule.contains("\"")) {
            return currentRule.replaceAll("[\" ]", "");
        }
        final StringBuilder pattern = new StringBuilder("(");
        var strs = currentRule.split("\\|");
        for (int i = 0; i < strs.length; i++) {
            allNums(strs[i]).stream()
                    .map(num -> getPattern(num.intValue(), rules))
                    .forEach(pattern::append);
            if (i != strs.length - 1)
                pattern.append("|");
        }
        return pattern.append(")").toString();
    }
}

