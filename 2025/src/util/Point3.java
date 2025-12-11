package util;

public record Point3(int x, int y, int z) {
    public static Point3 fromString(String str) {
        var strs = str.split(",");
        return new Point3(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]), Integer.parseInt(strs[2]));
    }
    public double distance(Point3 that) {
        return Math.sqrt(Math.pow(this.x() - that.x(), 2) + Math.pow(this.y() - that.y(), 2) + Math.pow(this.z() - that.z(), 2));
    }
}
