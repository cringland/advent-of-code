package twentytwenty.day3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/3
    public static void main(String[] args) throws IOException {
        List<String> patterns = Files.readAllLines(Paths.get("src/twentytwenty/day3/input"));
        long r1d1 = treesHit(patterns, 1, 1);
        long r3d1 = treesHit(patterns, 3, 1);
        long r5d1 = treesHit(patterns, 5, 1);
        long r7d1 = treesHit(patterns, 7, 1);
        long r1d2 = treesHit(patterns, 1, 2);
        long productTreesHit = r1d1 * r3d1 * r5d1 * r7d1 * r1d2;
        System.out.println("Problem 1: Product of all trees hit: " + productTreesHit);
        Util.assertEquals(252L, r3d1);
        System.out.println("Problem 2: Product of all trees hit: " + productTreesHit);
        Util.assertEquals(2608962048L, productTreesHit);
    }

    private static long treesHit(List<String> patterns, int right, int down) {
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
