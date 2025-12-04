import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Input {

    private final Path path;
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

    public Stream<Character> streamChars() {
        return lines.stream().flatMap(str -> str.chars().mapToObj(c -> (char) c));
    }
}
