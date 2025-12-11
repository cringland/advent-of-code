import util.Point2;

import java.util.*;

public class Day7 implements Day<Number> {

    @Override
    public Number sampleAnswerP1() {
        return 21;
    }

    @Override
    public Number sampleAnswerP2() {
        return 40L;
    }

    @Override
    public Number part1(Input input) {
        var lines = input.immutableLines();
        var startX = lines.getFirst().indexOf('S');
        var allX = Set.of(startX);
        var splitCount = 0;
        for (String line : lines.subList(1, lines.size())) {
            var newXes = new HashSet<Integer>();
            for (Integer x : allX) {
                if (line.charAt(x) == '^') {
                    splitCount++;
                    newXes.add(x - 1);
                    newXes.add(x + 1);
                } else {
                    newXes.add(x);
                }
            }
            allX = newXes;
        }
        return splitCount;
    }

    @Override
    public Number part2(Input input) {
        var lines = input.immutableLines();
        var startX = lines.getFirst().indexOf('S');
        var cache = new HashMap<Point2, Long>();
        var startPoint = new Point2(startX, 1);
        return timelines(lines, cache, startPoint);
    }

    private static Long timelines(List<String> lines, Map<Point2, Long> cache, Point2 currentBeam) {
        if(cache.containsKey(currentBeam)) {
            return cache.get(currentBeam);
        }
        if (currentBeam.y() == lines.size()-1) {
            return 1L;
        }
        if (lines.get(currentBeam.y()).charAt(currentBeam.x()) == '.') {
            var result = timelines(lines, cache, new Point2(currentBeam.x(), currentBeam.y() + 1));
            cache.put(currentBeam, result);
            return result;
        }
        var result1 = timelines(lines, cache, new Point2(currentBeam.x() + 1, currentBeam.y() + 1));
        var result2 = timelines(lines, cache, new Point2(currentBeam.x() - 1, currentBeam.y() + 1));
        cache.put(currentBeam, result1 + result2);
        return result1 + result2;
    }
}
