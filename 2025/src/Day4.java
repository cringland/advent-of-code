import java.util.ArrayList;
import java.util.List;

public class Day4 implements Day<Number> {
    public Number sampleAnswerP1() {
        return 13L;
    }

    public Number sampleAnswerP2() {
        return 43L;
    }

    public Number part1(Input input) {
        var lines = input.immutableLines();
        var count = 0L;
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).length(); j++) {
                if (lines.get(i).charAt(j) == '.') {
                    continue;
                }
                var pt = new Point2(j, i);
                var coveredAdjacents = pt.adjacent().stream().filter(it -> {
                    var inBounds = it.y() >= 0 && it.y() < lines.size() && it.x() >= 0 && it.x() < lines.get(it.y()).length();
                    return inBounds && lines.get(it.y()).charAt(it.x()) == '@';
                }).count();
                if (coveredAdjacents < 4) {
                    count++;
                }
            }
        }
        return count;
    }

    public Number part2(Input input) {
        var lines = input.mutableGrid();
        var count = 0L;
        var lastCount = 0L;
        do {
            lastCount = 0L;
            for (int i = 0; i < lines.size(); i++) {
                for (int j = 0; j < lines.get(i).size(); j++) {
                    if (lines.get(i).get(j) == '.') {
                        continue;
                    }
                    var pt = new Point2(j, i);
                    var coveredAdjacents = pt.adjacent().stream().filter(it -> {
                        var inBounds = it.y() >= 0 && it.y() < lines.size() && it.x() >= 0 && it.x() < lines.get(it.y()).size();
                        return inBounds && lines.get(it.y()).get(it.x()) == '@';
                    }).count();
                    if (coveredAdjacents < 4) {
                        count++;
                        lastCount++;
                        lines.get(i).set(j, '.');
                    }
                }
            }
        } while (lastCount!=0);
        return count;
    }

    record Point2(int x, int y) {
        public List<Point2> adjacent() {
            var list = new ArrayList<Point2>();
            for (int i = -1; i < 2; i++) {
                for (int j = -1; j < 2; j++) {
                    if (!(i == 0 && j == 0)) {
                        list.add(new Point2(x + i, y + j));
                    }
                }
            }
            return list;
        }

    }
}
