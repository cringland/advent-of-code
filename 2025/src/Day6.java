import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day6 implements Day<Number> {
    @Override
    public Number sampleAnswerP1() {
        return 4277556L;
    }

    @Override
    public Number sampleAnswerP2() {
        return null;
    }

    @Override
    public Number part1(Input input) {
        var lines = input.linesStream()
                .map(it -> Arrays.stream(it.split(" ")).filter(str -> !str.isEmpty()).toList()).toList();
        var nums = new ArrayList<Long>();
        for (int i = 0; i < lines.getFirst().size(); i++) {
            var num = i;
            var column = lines.stream().map(it -> it.get(num)).toList();
            var columnNumbers = column.stream().takeWhile(it -> !(it.equals("+") || it.equals("*"))).map(Long::parseLong).toList();
            var sign = column.getLast();
            if (sign.equals("+")) {
                nums.add(Util.sumLong(columnNumbers));
            } else {
                nums.add(Util.productLong(columnNumbers));
            }
        }
        return Util.sumLong(nums);
    }

    @Override
    public Number part2(Input input) {
        var lines = input.immutableLines();
        List<Character> lastLine = Util.toCharList(lines.getLast().substring(1));
        var sizes = new ArrayList<Integer>();
        var signs = lines.getLast().replace(" ", "");
        for (int i = 0; i < signs.length(); i++) {
            var next = lastLine.stream().takeWhile(it -> it != '+' && it != '*').toList();
            sizes.add(next.size());
            if (i != signs.length()-1)
                lastLine = lastLine.subList(next.size() + 1, lastLine.size());
        }
        sizes.set(sizes.size() - 1, 2);
//        ????? Everything about this is awful. It cant read the last size.

        var splitNums = lines.subList(0, lines.size() - 1).stream()
                .map(it -> {
                    var remainder = it;
                    var strs = new ArrayList<String>();
                    for (int i = 0; i < sizes.size(); i++) {
                        var size = sizes.get(i);
                        var current = remainder.substring(0, size);
                        strs.add(current);
                        remainder = remainder.substring(Math.min(remainder.length() - 1, size + 1));
                    }
                    return strs;
                }).toList();

        var nums = new ArrayList<Long>();
        for (int i = 0; i < signs.length(); i++) {
            var idx = i;
            var column = splitNums.stream().map(it -> it.get(idx)).toList();
            var mutatedColumn = new ArrayList<Long>();
            for (int j = 0; j < sizes.get(i); j++) {
                var jay = j;
                var chars = column.stream().map(it -> String.valueOf(it.charAt(jay))).collect(Collectors.joining()).trim();
                mutatedColumn.add(Long.valueOf(chars));
            }
            var sign = signs.charAt(idx);
            if (sign == '+') {
                nums.add(Util.sumLong(mutatedColumn));
            } else {
                nums.add(Util.productLong(mutatedColumn));
            }
        }
        return Util.sumLong(nums);

    }
}
