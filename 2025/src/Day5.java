import util.Util;

import java.util.*;
import java.util.stream.Collectors;


public class Day5 implements Day<Number> {
    record Range(long left, long right) {
        public boolean incRange(long num) {
            return num >= left && num <= right;
        }

        public long diff() {
            return (right - left) + 1;
        }
    }

    @Override
    public Number sampleAnswerP1() {
        return 3;
    }

    @Override
    public Number sampleAnswerP2() {
        return 14L;
    }

    @Override
    public Number part1(Input input) {
        var strs = input.twoLists();
        var ranges = strs.one().stream().map(it -> {
            var nums = it.split("-");
            return new Range(Long.parseLong(nums[0]), Long.parseLong(nums[1]));
        }).toList();
        var nums = strs.two().stream().map(Long::parseLong)
                .filter(num -> ranges.stream().anyMatch(range -> range.incRange(num)))
                .toList();
        return nums.size();
    }

    @Override
    public Number part2(Input input) {
        var strs = input.twoLists();
        var ranges = strs.one().stream().map(it -> {
            var nums = it.split("-");
            return new Range(Long.parseLong(nums[0]), Long.parseLong(nums[1]));
        }).sorted(Comparator.comparing(Range::left)).toList();
        var previousSize = ranges.size();
        do {
            previousSize = ranges.size();
            ranges = squash(ranges);
        } while (previousSize != ranges.size());
        return Util.sumLong(ranges.stream().map(Range::diff).collect(Collectors.toList()));
    }

    private static List<Range> squash(List<Range> ranges) {
        var newRanges = new ArrayList<Range>();
        newRanges.add(ranges.getFirst());
        for (var range : ranges) {
            var prevRange = newRanges.getLast();
            if (range.left() <= prevRange.right() + 1) {
                newRanges.set(newRanges.size() - 1, new Range(prevRange.left(), Long.max(range.right(), prevRange.right())));
            } else {
                newRanges.add(range);
            }
        }
        return newRanges;
    }
}
