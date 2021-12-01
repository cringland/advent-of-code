package twentytwenty.day1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/1
    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Paths.get("src/twentytwenty/day1/input")).stream()
                .map(Integer::valueOf)
                .sorted()
                .collect(Collectors.toList());
            
        for (int i = 0, j = input.size() - 1; i < j; ) {
            final int in1 = input.get(i), in2 = input.get(j);
            int sum = in1 + in2;
            if (sum < 2020) {
                i++;
            } else if (sum > 2020) {
                j--;
            } else {
                int answer = in1 * in2;
                System.out.println("Problem 1: Answer is: " + (in1 * in2));
                Util.assertEquals(996075, answer);
                break;
            }
        }

        for (var i = 0; i < input.size(); i++)
            for (var j = i + 1; j < input.size(); j++)
                for (var k = j + 1; k < input.size(); k++)
                    if (input.get(i) + input.get(j) + input.get(k) == 2020) {
                        var answer = input.get(i) * input.get(j) * input.get(k);
                        Util.assertEquals(51810360, answer);
                        System.out.println("Problem 2 Answer is: " + answer);
                        break;
                    }
    }
}
