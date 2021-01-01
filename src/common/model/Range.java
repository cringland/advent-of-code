package common.model;

public class Range {

    private Long lowest;
    private Long highest;

    public static Range of(Long lowest, Long highest) {
        Range range = new Range();
        range.lowest = lowest;
        range.highest = highest;
        return range;
    }

    public static Range of(Integer lowest, Integer highest) {
        return Range.of(lowest.longValue(), highest.longValue());
    }

    public Long lowest() {
        return lowest;
    }

    public Long highest() {
        return highest;
    }

    public Long sum() {
        return highest + lowest;
    }

    public Long diff() {
        return highest - lowest;
    }

    public boolean testInclusive(final Long num) {
        return num <= highest && num >= lowest;
    }

    @Override
    public String toString() {
        return "Range{" +
                "lowest=" + lowest +
                ", highest=" + highest +
                '}';
    }
}
