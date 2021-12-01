package day17;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import common.Util;

public class Problem {

    //https://adventofcode.com/2020/day/17
    public static void main(String[] args) throws IOException {
        var input = Files.readAllLines(Paths.get("src/twentytwenty/day17/input")).stream()
                .map(str -> str.chars().mapToObj(c -> c == '#').collect(Collectors.toList()))
                .collect(Collectors.toList());

        var grid3 = new Grid();
        var grid4 = new Grid();
        for (int x = 0; x < input.size(); x++) {
            for (int y = 0; y < input.get(x).size(); y++) {
                if (input.get(x).get(y)) {
                    grid3.setActive(x, y, 0);
                    grid4.setActive(x, y, 0);
                }
            }
        }

        for (int i = 0; i < 6; i++) {
            grid3 = grid3.mutate3();
            grid4 = grid4.mutate4();
        }

        System.out.println("Problem 1 Answer is: " + grid3.activeCount());
        Util.assertEquals(267L, grid3.activeCount());
        System.out.println("Problem 2 Answer is: " + grid4.activeCount());
        Util.assertEquals(1812L, grid4.activeCount());
    }
}

class Grid {

    private final Map<Point, Boolean> grid = new HashMap<>();
    private int maxX = 0;
    private int maxY = 0;
    private int maxZ = 0;
    private int maxW = 0;

    private int minX = 0;
    private int minY = 0;
    private int minZ = 0;
    private int minW = 0;

    public boolean isActive(int x, int y, int z) {
        return isActive(x, y, z, 0);
    }

    public boolean isActive(int x, int y, int z, int w) {
        Boolean b = grid.get(new Point(x, y, z, w));
        return b != null ? b : false;
    }

    public void setActive(int x, int y, int z, int w) {
        maxX = Math.max(x + 1, maxX);
        maxY = Math.max(y + 1, maxY);
        maxZ = Math.max(z + 1, maxZ);
        maxW = Math.max(w + 1, maxW);
        minX = Math.min(x - 1, minX);
        minY = Math.min(y - 1, minY);
        minZ = Math.min(z - 1, minZ);
        minW = Math.min(w - 1, minW);
        grid.put(new Point(x, y, z, w), true);
    }

    public void setActive(int x, int y, int z) {
        setActive(x, y, z, 0);
    }

    public void setInactive(int x, int y, int z) {
        setInactive(x, y, z, 0);
    }

    public void setInactive(int x, int y, int z, int w) {
        maxX = Math.max(x + 1, maxX);
        maxY = Math.max(y + 1, maxY);
        maxZ = Math.max(z + 1, maxZ);
        maxW = Math.max(w + 1, maxW);
        minX = Math.min(x - 1, minX);
        minY = Math.min(y - 1, minY);
        minZ = Math.min(z - 1, minZ);
        minW = Math.min(w - 1, minW);
        grid.put(new Point(x, y, z, w), false);
    }

    private int getNeighbours(int X, int Y, int Z) {
        int count = 0;
        for (int x = X - 1; x < X + 2; x++) {
            for (int y = Y - 1; y < Y + 2; y++) {
                for (int z = Z - 1; z < Z + 2; z++) {
                    if (!(x == X && y == Y && z == Z)
                            && isActive(x, y, z)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int getNeighbours4(int X, int Y, int Z, int W) {
        int count = 0;
        for (int x = X - 1; x < X + 2; x++) {
            for (int y = Y - 1; y < Y + 2; y++) {
                for (int z = Z - 1; z < Z + 2; z++) {
                    for (int w = W - 1; w < W + 2; w++) {
                        if (!(x == X && y == Y && z == Z && w == W)
                                && isActive(x, y, z, w)) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    public Grid mutate3() {
        var newGrid = new Grid();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    var neighbours = getNeighbours(x, y, z);
                    var isActive = isActive(x, y, z);
                    if ((isActive && (neighbours == 2 || neighbours == 3))
                            || (!isActive && neighbours == 3)) {
                        newGrid.setActive(x, y, z);
                    } else {
                        newGrid.setInactive(x, y, z);
                    }
                }
            }
        }
        return newGrid;
    }

    public Grid mutate4() {
        var newGrid = new Grid();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    for (int w = minW; w <= maxW; w++) {
                        var neighbours = getNeighbours4(x, y, z, w);
                        var isActive = isActive(x, y, z, w);
                        if ((isActive && (neighbours == 2 || neighbours == 3))
                                || (!isActive && neighbours == 3)) {
                            newGrid.setActive(x, y, z, w);
                        } else {
                            newGrid.setInactive(x, y, z, w);
                        }
                    }
                }
            }
        }
        return newGrid;
    }

    public Long activeCount() {
        return grid.values().stream().filter(val -> val).count();
    }
}

class Point {

    private final int x;
    private final int y;
    private final int z;
    private final int w;

    public Point(final int x, final int y, final int z, final int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        final Point point = (Point) o;
        return x == point.x &&
                y == point.y &&
                z == point.z &&
                w == point.w;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, w);
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + "," + w + ']';
    }
}
