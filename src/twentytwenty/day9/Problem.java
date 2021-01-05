package twentytwenty.day9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

import common.Util;

import static common.Util.range;
import static common.Util.sum;

public class Problem {

    //https://adventofcode.com/2020/day/9
    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Paths.get("src/twentytwenty/day9/input")).stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        var preamble = 25;
        var problem1Answer = 0L;
        for (int i = preamble; i < input.size(); i++) {
            var current = input.get(i);
            var valid = false;
            for (var j = i - preamble; j < i && !valid; j++) {
                for (var k = j + 1; k < i && !valid; k++) {
                    var sum = input.get(j) + input.get(k);
                    if (sum == input.get(i)) {
                        valid = true;
                    }
                }
            }
            if (!valid) {
                problem1Answer = current;
                System.out.println("Problem 1 Answer is: " + problem1Answer);
                Util.assertEquals(133015568L, problem1Answer);
                break;
            }
        }

        Queue<Long> contiguousNums = new LinkedList<>();
        for (Long l : input) {
            contiguousNums.add(l);
            while (sum(contiguousNums) > problem1Answer) {
                contiguousNums.poll();
            }
            if (sum(contiguousNums) == problem1Answer) {
                break;
            }
        }
        long problem2Answer = range(contiguousNums).sum();

        System.out.println("Problem 2 Answer is: " + problem2Answer);
        Util.assertEquals(16107959L, problem1Answer);
    }
}
