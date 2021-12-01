package day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import common.Util;
import common.model.Range;

public class Problem {

    //https://adventofcode.com/2020/day/3
    public static void main(String[] args) throws IOException {
        List<String> patterns = Files.readAllLines(Paths.get("src/twentytwenty/day3/input"));
        var problem1 = treesHit(patterns, Range.of(3, 1));
        var productTreesHit = List.of(new Range[]{
                Range.of(1, 1),
                Range.of(5, 1),
                Range.of(7, 1),
                Range.of(1, 2)
        }).stream().map(r -> treesHit(patterns, r)).reduce(1L, Math::multiplyExact);
        productTreesHit *= problem1;
        System.out.println("Problem 1: Trees hit for right 3 down 1: " + problem1);
        Util.assertEquals(162L, problem1);
        System.out.println("Problem 2: Product of all trees hit: " + productTreesHit);
        Util.assertEquals(3064612320L, productTreesHit);
    }

    private static long treesHit(List<String> patterns, Range range) {
        int right = range.lowest().intValue(), down = range.highest().intValue();
        int treesHit = 0;
        for (int i = 0, j = 0; j < patterns.size(); j += down) {
            String pattern = patterns.get(j);
            if (pattern.charAt(i) == '#')
                treesHit++;
            i = (i + right) % pattern.length();
        }
        return treesHit;
    }
}
