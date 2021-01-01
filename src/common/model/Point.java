package common.model;

import java.util.Objects;

public class Point {

    private Long x;
    private Long y;

    public static Point of(Long x, Long y) {
        Point range = new Point();
        range.x = x;
        range.y = y;
        return range;
    }

    public static Point of(Integer x, Integer y) {
        Point range = new Point();
        range.x = x.longValue();
        range.y = y.longValue();
        return range;
    }

    public Long x() {
        return x;
    }

    public Long y() {
        return y;
    }

    public Point addX(int x) {
        return Point.of(this.x + x, this.y);
    }

    public Point addY(int y) {
        return Point.of(this.x, this.y + y);
    }

    public Point add(Point that) {
        return Point.of(this.x + that.x(), this.y + that.y());
    }

    public Point times(int times) {
        return Point.of(this.x * times, this.y * times);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        final Point point = (Point) o;
        return Objects.equals(x, point.x) &&
                Objects.equals(y, point.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ']';
    }
}
