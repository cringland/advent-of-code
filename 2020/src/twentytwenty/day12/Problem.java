package twentytwenty.day12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import common.Util;
import common.model.Point;

public class Problem {

    //https://adventofcode.com/2020/day/12
    public static void main(String[] args) throws IOException {
        final var input = Files.readAllLines(Paths.get("src/twentytwenty/day12/input"));

        long problem1Answer = getProblem1Answer(input);
        System.out.println("Problem 1 Answer is: " + problem1Answer);
        Util.assertEquals(1294L, problem1Answer);
        var ship = Point.of(0, 0);
        var waypoint = Point.of(10, 1);
        for (var str : input) {
            Character newDir = str.charAt(0);
            var num = Integer.valueOf(str.substring(1));
            switch (newDir) {
                case 'R':
                    waypoint = rotate(waypoint, num);
                    break;
                case 'L':
                    waypoint = rotate(waypoint, -num);
                    break;
                case 'F':
                    ship = ship.add(waypoint.times(num));
                    break;
                default:
                    waypoint = move(waypoint, Dir.valueOf(newDir.toString()), num);
            }
        }
        var problem2Answer = Math.abs(ship.x()) + Math.abs(ship.y());
        System.out.println("Problem 2 Answer is: " + problem2Answer);
        Util.assertEquals(20592L, problem1Answer);
    }

    private static long getProblem1Answer(final List<String> input) {
        var eastNorth = Point.of(0, 0);
        var dir = Dir.E;
        for (var str : input) {
            Character newDir = str.charAt(0);
            var num = Integer.valueOf(str.substring(1));
            switch (newDir) {
                case 'R':
                    dir = dir.turn(num);
                    break;
                case 'L':
                    dir = dir.turn(-num);
                    break;
                case 'F':
                    eastNorth = move(eastNorth, dir, num);
                    break;
                default:
                    eastNorth = move(eastNorth, Dir.valueOf(newDir.toString()), num);

            }
        }
        return Math.abs(eastNorth.x()) + Math.abs(eastNorth.y());
    }

    private static Point move(Point start, Dir dir, int dist) {
        switch (dir) {
            case E:
                return start.addX(dist);
            case S:
                return start.addY(-dist);
            case W:
                return start.addX(-dist);
            case N:
                return start.addY(dist);
        }
        throw new RuntimeException();
    }

    private static Point rotate(Point pivot, int degree) {
        int rotations = degree / 90;
        int clockWiseRotations = rotations < 0 ? 4 + rotations : rotations;
        var east = pivot.x();
        var north = pivot.y();
        for (int i = 0; i < clockWiseRotations; i++) {
            var temp = north;
            north = -east;
            east = temp;
        }
        return Point.of(east, north);
    }
}

enum Dir {
    E(0), S(1), W(2), N(3);

    int val;

    Dir(int val) {
        this.val = val;
    }

    public Dir turn(int degree) {
        int newVal = (val + (degree / 90)) % 4;
        final int newerVal = newVal < 0 ? 4 + newVal : newVal;
        return Arrays.stream(values()).filter(dir -> dir.val == newerVal).findFirst().get();
    }
}
