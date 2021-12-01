package twentytwenty.day20;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import common.Matrix;
import common.StringUtil;
import common.Util;
import common.model.Point;

import static common.StringUtil.firstNum;

public class Problem {

    private static final String SEA_MONSTER =
                      "                  # \n"
                    + "#    ##    ##    ###\n"
                    + " #  #  #  #  #  #   ";

    private static final List<Point> SM_INDICES = getIndexes();

    private static List<Point> getIndexes() {
        var points = new ArrayList<Point>();
        var lines = SEA_MONSTER.split("\n");
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                if (lines[y].charAt(x) == '#') {
                    points.add(Point.of(x, y));
                }
            }
        }
        return points;
    }

    private static Predicate<Matrix<Character>> isSM = matrix -> {
        for (var point : SM_INDICES) {
            if (!matrix.get(point.x().intValue(), point.y().intValue()).equals('#'))
                return false;
        }
        return true;
    };

    //https://adventofcode.com/2020/day/20
    public static void main(String[] args) throws IOException {
        var tiles = Stream.of(Files.readString(Paths.get("src/twentytwenty/day20/input")).split("\n\n"))
                .map(Tile::parse)
                .collect(Collectors.toList());

        var pic = Problem.buildImage(tiles);

        System.out.println("Problem 1 Answer is: " + pic.productOfCorners());
        Util.assertEquals(18262194216271L, pic.productOfCorners());

        //This has to be manually done at the moment
        var cropped = pic.croppedImage().flippedHorizontal().rotated90().rotated90().rotated90();
        var seaMonsters = seaMonsters(cropped);
        System.out.println("Found sea monsters: " + seaMonsters);
        var hashCount = cropped.copy().stream().map(l -> l.stream().filter(c -> c.equals('#')).count()).reduce(Math::addExact).get();
        final long problem2Ans = hashCount - (seaMonsters * 15);
        System.out.println("Problem 2 Answer is: " + problem2Ans);
        Util.assertEquals(2023L, problem2Ans);
    }

    private static Image buildImage(List<Tile> tiles) {
        var mutations = tiles.stream()
                .map(Tile::mutations)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        var corners = mutations.stream()
                .filter(t1 ->
                        mutations.stream().filter(t2 -> !t1.equals(t2))
                                .noneMatch(t2 ->
                                        t1.getFirstCol().equals(t2.getLastCol()) || t1.getFirstRow().equals(t2.getLastCol())
                                )
                )
                .distinct()
                .collect(Collectors.toList());

        var topLeft = corners.get(0);
        var leftSide = new ArrayList<Tile>();
        leftSide.add(topLeft);

        //Assuming image is a square for now
        for (int i = 1; i < Math.sqrt(tiles.size()); i++) {
            var previous = leftSide.get(i - 1);
            var current = mutations.stream()
                    .filter(tile -> !previous.equals(tile))
                    .filter(tile -> previous.getLastRow().equals(tile.getFirstRow()))
                    .findAny().get();
            leftSide.add(current);
        }

        var grid = new ArrayList<List<Tile>>();
        for (var tile : leftSide) {
            var row = new ArrayList<Tile>();
            row.add(tile);
            for (int i = 1; i < Math.sqrt(tiles.size()); i++) {
                var previous = row.get(i - 1);
                var current = mutations.stream()
                        .filter(t2 -> !previous.equals(t2))
                        .filter(t2 -> previous.getLastCol().equals(t2.getFirstCol()))
                        .findAny().get();
                row.add(current);
            }
            grid.add(row);
        }
        return new Image(grid);
    }

    private static int seaMonsters(Matrix<Character> image) {
        final int size = image.copy().size();
        int count = 0;
        for (int x = 0; x < size - 20; x++) {
            for (int y = 0; y < size - 3; y++) {
                var subMatrix = image.subMatrix(x, x + 20, y, y + 3);
                if (isSM.test(subMatrix)) {
                    count++;
                }
            }
        }
        return count;
    }
}

class Image {

    private Matrix<Tile> tiles;

    public Image(final List<List<Tile>> tiles) {
        this.tiles = new Matrix<>(tiles);
    }

    public Long productOfCorners() {
        var fRow = this.tiles.getFirstRow();
        var lRow = this.tiles.getLastRow();
        return fRow.get(0).getId() * fRow.get(fRow.size() - 1).getId() *
                lRow.get(0).getId() * lRow.get(lRow.size() - 1).getId();
    }

    public Matrix<Character> croppedImage() {
        var croppedCopy = tiles.copy().stream()
                .map(r -> r.stream().map(Tile::cropped).collect(Collectors.toList()))
                .collect(Collectors.toList());
        var list = new ArrayList<List<Character>>();

        for (var tileRow : croppedCopy) {
            var temp = new ArrayList<List<Character>>();
            for (var tile : tileRow) {
                var grid = tile.grid();
                if (temp.isEmpty()) {
                    temp.addAll(grid);
                } else {
                    for (int i = 0; i < grid.size(); i++) {
                        temp.get(i).addAll(grid.get(i));
                    }
                }
            }
            list.addAll(temp);
        }
        return new Matrix<>(list);
    }
}

class Tile {

    private Long id;
    private Matrix<Character> matrix;

    public static Tile parse(String str) {
        var lines = List.of(str.split("\n"));
        var id = firstNum(lines.get(0));
        return new Tile(id, toMatrix(lines.subList(1, lines.size())));
    }

    public Tile(final Long id, final Matrix<Character> matrix) {
        this.id = id;
        this.matrix = matrix;
    }

    public List<Tile> mutations() {
        var list = new ArrayList<Tile>();
        Tile copy = this;
        list.add(copy);
        for (int i = 0; i < 3; i++) {
            copy = copy.rotated90();
            list.add(copy);
        }
        copy = copy.flipped();
        list.add(copy);
        for (int i = 0; i < 3; i++) {
            copy = copy.rotated90();
            list.add(copy);
        }
        return list;
    }

    public Tile flipped() {
        return new Tile(this.id, matrix.flippedHorizontal());
    }

    public Tile rotated90() {
        return new Tile(this.id, this.matrix.rotated90());
    }

    public Tile cropped() {
        var copy = matrix.copy();
        copy = copy.subList(1, copy.size() - 1)
                .stream().map(l -> l.subList(1, l.size() - 1))
                .collect(Collectors.toList());
        return new Tile(this.id, new Matrix<>(copy));
    }

    public Long getId() {
        return id;
    }

    public List<Character> getFirstCol() {
        return matrix.getFirstCol();
    }

    public List<Character> getLastCol() {
        return matrix.getLastCol();
    }

    public List<Character> getFirstRow() {
        return matrix.getFirstRow();
    }

    public List<Character> getLastRow() {
        return matrix.getLastRow();
    }

    public List<List<Character>> grid() {
        return matrix.copy();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;
        final Tile tile = (Tile) o;
        return Objects.equals(id, tile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tile: " + id;
    }

    private static Matrix<Character> toMatrix(final List<String> strs) {
        return new Matrix<>(strs.stream().map(StringUtil::toList).collect(Collectors.toList()));
    }
}
