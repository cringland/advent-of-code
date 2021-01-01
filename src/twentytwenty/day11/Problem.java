package twentytwenty.day11;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import common.model.Point;

public class Problem {

    //https://adventofcode.com/2020/day/11
    public static void main(String[] args) throws IOException {
        final var input = Files.readAllLines(Paths.get("src/twentytwenty/day11/input")).stream()
                .map(str -> str.chars().mapToObj(c -> (char) c).toArray(Character[]::new))
                .map(List::of)
                .collect(Collectors.toList());

        List<List<Character>> oldLayout;
        List<List<Character>> newLayout = input;

        do {
            print(newLayout);
            System.out.println();
            oldLayout = newLayout;
            newLayout = update(oldLayout);
        } while (!oldLayout.equals(newLayout));

        var occs = newLayout.stream().reduce(0L,
                ((i, l) -> i + l.stream()
                        .filter(c -> c.equals('#'))
                        .count()),
                Math::addExact);
        System.out.println("Problem 1 Answer is: " + occs);
    }

    private static void printATester(final List<List<Character>> input, Point point) {
        var test = emptyMatrix(input);
        visibleHash(test, (input.size() / 2) - 1, (input.get(0).size() / 2) - 1, point.x().intValue(), point.y().intValue());
        System.out.println();
        print(test);
    }

    private static List<List<Character>> update(final List<List<Character>> input) {
        var newSeats = emptyMatrix(input);
        for (var i = 0; i < input.size(); i++) {
            for (var j = 0; j < input.get(i).size(); j++) {
                final var current = input.get(i).get(j);
                var newChar = current;
                if (current != '.') {
//                    var numOfOccs = getAdjacents(input, i, j).stream()
//                            .filter(c -> c.equals('#'))
//                            .count();
                    var numOfOccs = visibleHashes(input, i, j);
                    if (current == 'L' && numOfOccs == 0L) {
                        newChar = '#';
                    } else if (current == '#' && numOfOccs >= 5L) {
//                    } else if (current == '#' && numOfOccs >= 4) {
                        newChar = 'L';
                    }
                }
                newSeats.get(i).set(j, newChar);
            }
        }
        return newSeats;
    }

    private static List<List<Character>> emptyMatrix(final List<List<Character>> input) {
        final var grid = new ArrayList<List<Character>>();
        input.forEach(row -> {
            grid.add(new ArrayList<>());
            row.forEach(c -> grid.get(grid.size() - 1).add('.'));
        });
        return grid;
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

    private static boolean visibleHash(final List<List<Character>> input, final int startX, final int startY,
                                       final int plusX, final int plusY) {
        for (int x = startX, y = startY; x < input.size() && x >= 0 && y < input.get(x).size() && y >= 0; x += plusX, y += plusY)
            if (!(x == startX && y == startY))
                if (input.get(x).get(y).equals('#'))
                    return true;
                else if (input.get(x).get(y).equals('L'))
                    return false;
//            if (x == startX && y == startY)
//                input.get(x).set(y, 'X');
//            else
//                input.get(x).set(y, 'Y');
        return false;
    }


    private static void print(final List<List<Character>> input) {
        input.forEach(l -> {
                    System.out.print("\n");
                    l.forEach(System.out::print);
                }
        );
    }


}
