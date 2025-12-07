import util.Util;

import java.util.*;
import java.util.function.Predicate;


public class Day2 implements Day<Number> {

    record Range(long left, long right) {
    }

    public Number sampleAnswerP1() {
        return 1227775554L;
    }

    public Number sampleAnswerP2() {
        return 4174379265L;
    }

    public Number part1(Input input) {
        return solve(input, Day2::isInvalid);
    }

    public Number part2(Input input) {
        return solve(input, Day2::isInvalidP2);
    }

    private Long solve(Input input, Predicate<Long> invalidityCheck) {
        return Arrays.stream(input.oneString().split(","))
                .map(it -> {
                    var strs = it.split("-");
                    return new Range(Long.parseLong(strs[0]), Long.parseLong(strs[1]));
                })
                .map(range -> {
                    List<Long> n = new ArrayList<>();
                    for (var i = range.left; i <= range.right; i++) {
                        if (invalidityCheck.test(i)) {
                            n.add(i);
                        }
                    }
                    return Util.sumLong(n);
                })
                .reduce(0L, Long::sum);
    }

    private static boolean isInvalidP2(long num) {
        var string = String.valueOf(num);
        for (int i = string.length() / 2; i > 0; i--) {
            if (string.length() % i > 0) continue;
            var chunks = Util.chunkList(string, i);
            if (chunks.stream().allMatch(chunks.getFirst()::equals)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isInvalid(long num) {
        var string = String.valueOf(num);
        return string.substring(0, string.length() / 2).equals(string.substring(string.length() / 2));
    }
}
