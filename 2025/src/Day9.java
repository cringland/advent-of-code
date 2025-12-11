import util.SamePair;
import util.Point2;

import java.util.*;

public class Day9 implements Day<Number> {
    @Override
    public Number sampleAnswerP1() {
        return 50L;
    }

    @Override
    public Number sampleAnswerP2() {
        return 24L;
    }

    @Override
    public Number part1(Input input) {
        return uniquePairs(input).stream().map(Day9::area).max(Comparator.naturalOrder()).get();
    }

    @Override
    public Number part2(Input input) {
        var points = new ArrayList<>(input.point2s());
        points.add(points.getFirst());
        var inShapeCache = new HashMap<Point2, Boolean>();
        var uniquePairs = uniquePairs(input).stream().sorted(Comparator.comparing(Day9::area)).toList().reversed();
        for (SamePair<Point2> it : uniquePairs) {
            if (cornersAndEdges(it).stream().allMatch(pt -> inShape(points, inShapeCache, pt))) {
                return area(it);
            }
        }
        //Sort for largest and then find first that all are in shape;
        //Terribly slow
        return -1;
    }

    private boolean inShape(List<Point2> shape, Map<Point2, Boolean> cache, Point2 pointToCheck) {
        if (cache.containsKey(pointToCheck)) {
            return cache.get(pointToCheck);
        }
        var inShape = false;
        for (int i = 0; i < shape.size() - 1; i++) {
            var a = shape.get(i);
            var b = shape.get(i + 1);
            if (pointToCheck.equals(a)) {
                // point is a corner
                cache.put(pointToCheck, true);
                return true;
            }

            if ((a.y() > pointToCheck.y()) != (b.y() >= pointToCheck.y())) {
                var slope = (pointToCheck.x() - a.x()) * (b.y() - a.y()) - (b.x() - a.x()) * (pointToCheck.y() - a.y());
                if (slope == 0) {
                    // point is on boundary
                    cache.put(pointToCheck, true);
                    return true;
                }
                if ((slope < 0) != (b.y() < a.y())) {
                    inShape = !inShape;
                }
            }
        }
        cache.put(pointToCheck, inShape);
        return inShape;
    }

    private static Long area(SamePair<Point2> it) {
        var p1 = it.left();
        var p2 = it.right();
        return (Math.abs(((long) p2.x()) - p1.x()) + 1) * (Math.abs((p2.y()) - p1.y()) + 1);
    }

    private static Set<Point2> cornersAndEdges(SamePair<Point2> it) {
        var p1 = it.left();
        var p2 = it.right();
        var list = new HashSet<Point2>();

        var minX = Math.min(p1.x(), p2.x());
        var maxX = Math.max(p1.x(), p2.x());
        var minY = Math.min(p1.y(), p2.y());
        var maxY = Math.max(p1.y(), p2.y());

//      Dont need to check corners
        for (int x = minX+1; x < maxX; x++) {
            list.add(new Point2(x, minY));
            list.add(new Point2(x, maxY));
        }

        for (int y = minY+1; y <= maxY-1; y++) {
            list.add(new Point2(minX, y));
            list.add(new Point2(maxX, y));
        }
        return list;
    }

    private static Set<Point2> points(SamePair<Point2> it) {
//      Gets all points inside a rectangle. Far too slow
        var p1 = it.left();
        var p2 = it.right();
        var list = new HashSet<Point2>();
        for (int x = Math.min(p1.x(), p2.x()); x <= Math.max(p1.x(), p2.x()); x++) {
            for (int y = Math.min(p1.y(), p2.y()); y <= Math.max(p1.y(), p2.y()); y++) {
                list.add(new Point2(x, y));
            }

        }
        return list;
    }

    private static List<SamePair<Point2>> pairedLines(Input input) {
        var points = new ArrayList<>(input.point2s());
        points.add(points.getFirst());

        List<SamePair<Point2>> chunks = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            chunks.add(new SamePair<>(points.get(i), points.get(i + 1)));
        }
        return chunks;
    }

    private static List<SamePair<Point2>> uniquePairs(Input input) {
        var points = input.point2s();
        var pairs = new ArrayList<SamePair<Point2>>();
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                var p1 = points.get(i);
                var p2 = points.get(j);
                if (!p1.equals(p2)) {
                    var pair = new SamePair<>(p1, p2);
                    pairs.add(pair);
                }
            }
        }
        return pairs;
    }

}
