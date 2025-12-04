import java.util.ArrayList;
import java.util.List;

public class Util {
    public static Long sumLong(List<Long> list) {
        return list.stream().reduce(0L, Long::sum);
    }

    public static Integer sumInt(List<Integer> list) {
        return list.stream().reduce(0, Integer::sum);
    }

    public static List<String> chunkList(String string, int chunkSize) {
        List<String> chunks = new ArrayList<>();
        for (int i = 0; i < string.length(); i += chunkSize) {
            chunks.add(string.substring(i, Math.min(string.length(), i + chunkSize)));
        }
        return chunks;
    }
}
