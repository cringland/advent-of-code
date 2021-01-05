package twentytwenty.day24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Util;
import common.model.Point;

public class Problem {

    //https://adventofcode.com/2020/day/24
    public static void main(String[] args) throws IOException {
        var tiles = Files.readAllLines(Paths.get("src/twentytwenty/day24/input")).stream()
                .map(s -> {
                    var copy = s;
                    var list = new ArrayList<Dir>();
                    while (!copy.equals("")) {
                        var temp = copy;
                        var dir = Stream.of(Dir.values())
                                .filter(d -> temp.startsWith(d.name()))
                                .findFirst().get();
                        list.add(dir);
                        copy = copy.replaceFirst(dir.name(), "");
                    }
                    return list;
                })
                .map(list -> list.stream().map(Dir::getPoint).reduce(Point.of(0, 0), Point::add))
                .collect(Collectors.toMap(t -> t, t -> true, (b1, b2) -> !b1));

        var count1 = blackTileCount(tiles);
        System.out.println("Problem 1 Answer is: " + count1);
        Util.assertEquals(360L, count1);

        for (int i = 0; i < 100; i++) {
            tiles = mutate(tiles);
        }

        var count2 = blackTileCount(tiles);
        System.out.println("Problem 2 Answer is: " + count2);
        Util.assertEquals(3924L, count2);
    }

    private static long blackTileCount(final Map<Point, Boolean> tiles) {
        return tiles.values().stream().filter(b -> b).count();
    }

    private static Map<Point, Boolean> mutate(Map<Point, Boolean> org) {
        var whiteTiles = new HashSet<Point>();
        var blackTileMatches = org.entrySet().stream()
                .filter(Map.Entry::getValue).map(Map.Entry::getKey) //Black tiles only
                .map(p -> { //Count adj black tiles and add to white set if white
                    var blackCount = adjacents(org, p).stream()
                            .map(adj -> {
                                if (adj.getValue())
                                    return 1;
                                else
                                    whiteTiles.add(adj.getKey());
                                return 0;
                            }).reduce(0, Integer::sum);
                    return new AbstractMap.SimpleEntry<>(p, blackCount);
                })
                .filter(entry -> entry.getValue() == 0 || entry.getValue() > 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toMap(p -> p, p -> false));

        var whiteTileMatches = whiteTiles.stream()
                .map(p -> new AbstractMap.SimpleEntry<>(p, adjacents(org, p)))
                .map(p -> {
                    var blackCount = p.getValue().stream()
                            .map(entry -> {
                                if (entry.getValue())
                                    return 1;
                                return 0;
                            }).reduce(0, Integer::sum);
                    return new AbstractMap.SimpleEntry<>(p.getKey(), blackCount);
                })
                .filter(entry -> entry.getValue() == 2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toMap(p -> p, p -> true));

        var copy = new HashMap<>(org);
        copy.putAll(blackTileMatches);
        copy.putAll(whiteTileMatches);
        return copy;
    }

    private static Set<Map.Entry<Point, Boolean>> adjacents(Map<Point, Boolean> org, Point centre) {
        return Arrays.stream(Dir.values()).map(Dir::getPoint)
                .map(centre::add)
                .map(dir -> new AbstractMap.SimpleEntry<>(dir, org.getOrDefault(dir, false)))
                .collect(Collectors.toSet());
    }
}

enum Dir {
    e(1, 0), se(0, 1), sw(-1, +1), w(-1, 0), nw(0, -1), ne(1, -1);

    private Point point;

    Dir(int x, int y) {
        this.point = Point.of(x, y);
    }

    public Point getPoint() {
        return point;
    }

}
