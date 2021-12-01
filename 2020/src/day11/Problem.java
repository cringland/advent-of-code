package day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import common.StringUtil;
import common.Util;
import common.model.Point;

public class Problem {

    //https://adventofcode.com/2020/day/11
    public static void main(String[] args) throws IOException {
        final var input = Files.readAllLines(Paths.get("src/twentytwenty/day11/input")).stream()
                .map(StringUtil::toList)
                .collect(Collectors.toList());

        var p1 = solve(input, Problem::getNewCharP1);
        System.out.println("Problem 1 Answer is: " + p1);
        Util.assertEquals(2334L, p1);

        var p2 = solve(input, Problem::getNewCharP2);
        System.out.println("Problem 2 Answer is: " + p2);
        Util.assertEquals(2100L, p2);
    }

    private static Long solve(final List<List<Character>> input, final BiFunction<List<List<Character>>, Point, Character> mapper) {
        List<List<Character>> oldLayout;
        List<List<Character>> newLayout = input;

        do {
            oldLayout = newLayout;
            newLayout = update(oldLayout, mapper);
        } while (!oldLayout.equals(newLayout));
        return newLayout.stream().reduce(0L,
                (i, l) -> i + hashCount(l),
                Math::addExact);
    }

    private static List<List<Character>> update(final List<List<Character>> input,
                                                final BiFunction<List<List<Character>>, Point, Character> mapper) {
        var newSeats = emptyMatrix(input);
        for (var i = 0; i < input.size(); i++) {
            for (var j = 0; j < input.get(i).size(); j++) {
                var newChar = mapper.apply(input, Point.of(i, j));
                newSeats.get(i).set(j, newChar);
            }
        }
        return newSeats;
    }

    private static Character getNewCharP1(final List<List<Character>> input, final Point point) {
        var i = point.x().intValue();
        var j = point.y().intValue();
        var current = input.get(i).get(j);
        if (current != '.') {
            var numOfOccs = hashCount(getAdjacents(input, i, j));
            if (current == 'L' && numOfOccs == 0L) {
                return '#';
            } else if (current == '#' && numOfOccs >= 4L) {
                return 'L';
            }
        }
        return current;
    }

    private static Character getNewCharP2(final List<List<Character>> input, final Point point) {
        var i = point.x().intValue();
        var j = point.y().intValue();
        final var current = input.get(i).get(j);
        if (current != '.') {
            var numOfOccs = visibleHashes(input, i, j);
            if (current == 'L' && numOfOccs == 0L) {
                return '#';
            } else if (current == '#' && numOfOccs >= 5L) {
                return 'L';
            }
        }
        return current;
    }

    private static List<Character> getAdjacents(final List<List<Character>> input, final int i, final int j) {
        var adjs = new ArrayList<Character>();
        for (var x = Math.max(i - 1, 0); x < Math.min(i + 2, input.size()); x++) {
            for (var y = Math.max(j - 1, 0); y < Math.min(j + 2, input.get(x).size()); y++) {
                adjs.add(input.get(x).get(y));
            }
        }
        adjs.remove(input.get(i).get(j));  //It will contain itself
        return adjs;
    }

    private static Long visibleHashes(final List<List<Character>> input, final int x, final int y) {
        //List of points where x is m and y is k
        var lines = List.of(
                Point.of(0, 1), // East
                Point.of(1, 1), // South East
                Point.of(1, 0), // South
                Point.of(1, -1), // South West
                Point.of(0, -1), // West
                Point.of(-1, -1), // North West
                Point.of(-1, 0), // North
                Point.of(-1, 1) // North East
        );
        return lines.stream()
                .map(point -> visibleHash(input, x, y, point.x().intValue(), point.y().intValue()))
                .filter(b -> b)
                .count();
    }

    private static List<List<Character>> emptyMatrix(final List<List<Character>> input) {
        return input.stream()
                .map(list -> list.stream().map(c -> '.').collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private static boolean visibleHash(final List<List<Character>> input, final int startX, final int startY,
                                       final int plusX, final int plusY) {
        for (int x = startX, y = startY; x < input.size() && x >= 0 && y < input.get(x).size() && y >= 0; x += plusX, y += plusY)
            if (!(x == startX && y == startY))
                if (input.get(x).get(y).equals('#'))
                    return true;
                else if (input.get(x).get(y).equals('L'))
                    return false;
        return false;
    }

    private static long hashCount(final List<Character> l) {
        return l.stream().filter(c -> c.equals('#')).count();
    }
}
