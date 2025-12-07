
import util.Util;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Day3 implements Day<Number>{

    record IndexWithValue(int idx, int value) {
    }

    public Number sampleAnswerP1() {
        return 357L;
    }

    public Number sampleAnswerP2() {
        return 3121910778619L;
    }

    public Number part1(Input input) {
        var joltages = input.linesStream()
                .map(it -> {
                    var bigLeft = getInteger(it.charAt(0));
                    var bigLeftIdx = 0;
                    for (int i = 1; i < it.length() - 1; i++) {
                        if(bigLeft == 9) break;
                        var num = getInteger(it.charAt(i));
                        if (num > bigLeft) {
                            bigLeft = num;
                            bigLeftIdx = i;
                        }
                    }
                    var bigRight = getInteger(it.charAt(bigLeftIdx + 1));
                    for (int i = bigLeftIdx + 2; i < it.length(); i++) {
                        if(bigRight == 9) break;
                        var num = getInteger(it.charAt(i));
                        if (num > bigRight) {
                            bigRight = num;
                        }
                    }
                    return (bigLeft * 10) + bigRight;
                }).toList();

        return Util.sumInt(joltages).longValue();
    }

    public Number part2(Input input) {
        var joltages = input.linesStream()
                .map(it -> {
                    var size = 12;
                    var ints = new ArrayList<Integer>();
                    var currentIndex = 0;
                    for (int i = 0; i < size; i++) {
                        var maxAndIdx = getLargestInt(it, currentIndex, it.length() - size + i + 1);
                        currentIndex = maxAndIdx.idx() + 1;
                        ints.add(maxAndIdx.value());
                    }
                    return ints.stream().map(String::valueOf).collect(Collectors.joining(""));
                }).map(Long::parseLong).toList();
        return Util.sumLong(joltages);
    }

    private static IndexWithValue getLargestInt(String it, int start, int end) {
        int idx = start;
        int max = getInteger(it.charAt(start));
        for (int i = start; i < end; i++) {
            if(max == 9) break;
            var num = getInteger(it.charAt(i));
            if (num > max) {
                max = num;
                idx = i;
            }
        }
        return new IndexWithValue(idx, max);
    }

    private static Integer getInteger(char it) {
        return Integer.valueOf(String.valueOf(it));
    }
}
