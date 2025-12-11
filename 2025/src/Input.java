import util.Point2;
import util.Point3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Input {

    public record TwoString(String one, String two) {
    }

    public record TwoList(List<String> one, List<String> two) {
    }

    private final Path path;
    private final String string;
    private final List<String> lines;

    public static Input input(Class<? extends Day> clazz) {
        return new Input(clazz.getName().toLowerCase() + "/input");
    }

    public static Input test(Class<? extends Day> clazz) {
        return new Input(clazz.getName().toLowerCase() + "/test");
    }

    private Input(String resourcePath) {
        this.path = Path.of("src/resource/" + resourcePath);
        try {
            this.string = Files.readString(path);
            this.lines = Files.readAllLines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<Character>> mutableGrid() {
        var grid = new ArrayList<List<Character>>();
        lines.forEach(str -> {
            var chars = str.chars().mapToObj(c -> (char) c);
            grid.add(new ArrayList<>(chars.toList()));
        });
        return grid;
    }

    public List<String> mutableLines() {
        return new ArrayList<>(lines);
    }

    public List<String> immutableLines() {
        return linesStream().toList();
    }

    public Stream<String> linesStream() {
        return lines.stream();
    }

    public String oneString() {
        return String.join("", lines);
    }

    public TwoString twoStrings() {
        var strs = string.split("\n\n");
        return new TwoString(strs[0], strs[1]);
    }

    public TwoList twoLists() {
        var twoString = twoStrings();
        return new TwoList(Arrays.stream(twoString.one.split("\n")).toList(), Arrays.stream(twoString.two.split("\n")).toList());
    }

    public Stream<Character> streamChars() {
        return lines.stream().flatMap(str -> str.chars().mapToObj(c -> (char) c));
    }

    public List<Point2> point2s() {
        return linesStream().map(Point2::fromString).toList();
    }

    public List<Point3> point3s() {
        return linesStream().map(Point3::fromString).toList();
    }

}
