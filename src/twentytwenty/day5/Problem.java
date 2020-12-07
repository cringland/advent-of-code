package twentytwenty.day5;

import static java.lang.String.format;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/5
    public static void main(String[] args) throws IOException {
        var seats = Files.readAllLines(Paths.get("src/twentytwenty/day5/input")).stream()
                .map(str -> new Seat(binarySpacePartition(str.substring(0, 7), 'F'), binarySpacePartition(str.substring(7), 'L')))
                .sorted(Comparator.comparingInt(Seat::getId))
                .collect(Collectors.toList());

        var lastId = seats.get(0).getId() + 1; //Don't trigger on first seat
        var missingId = -1;
        var highestId = seats.get(seats.size() - 1).getId();
        for (Seat seat : seats) {
            if (seat.getId() != (lastId + 1)) {
                missingId = seat.getId() - 1;
            }
            lastId = seat.getId();
        }

        System.out.println("Problem 1: Highest ID = " + highestId);
        Util.assertEquals(878, highestId);

        System.out.println("Problem 2: Missing ID = " + missingId);
        Util.assertEquals(504, missingId);
    }

    private static int binarySpacePartition(String s, char high) {
        double higher = Math.pow(2, s.length()) - 1;
        double lower = 0;
        for (var i = 0; i < s.length(); i++) {
            if (s.charAt(i) == high) {
                higher -= Math.ceil((higher - lower) / 2);
            } else {
                lower += Math.ceil((higher - lower) / 2);
            }
        }
        return (int) lower;
    }

    private static class Seat {

        int row;
        int col;

        Seat(final int row, final int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public int getId() {
            return row * 8 + col;
        }

        @Override
        public String toString() {
            return format("Seat [%s, %s] ID = %s", row, col, getId());
        }
    }
}
