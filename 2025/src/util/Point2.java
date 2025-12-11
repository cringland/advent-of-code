package util;

import java.util.ArrayList;
import java.util.List;

public record Point2(int x, int y) {
    public static Point2 fromString(String str) {
        var strs = str.split(",");
        return new Point2(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]));
    }

    public List<Point2> adjacent() {
        var list = new ArrayList<Point2>();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == 0 && j == 0)) {
                    list.add(new Point2(x + i, y + j));
                }
            }
        }
        return list;
    }
}
