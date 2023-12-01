package util

val adjDirs = listOf<(Int, Int) -> Point2>(
    { x, y -> Point2(x + 1, y) },
    { x, y -> Point2(x, y + 1) },
    { x, y -> Point2(x, y - 1) },
    { x, y -> Point2(x - 1, y) })

val diagDirs = listOf<(Int, Int) -> Point2>(
    { x, y -> Point2(x - 1, y - 1) },
    { x, y -> Point2(x + 1, y + 1) },
    { x, y -> Point2(x + 1, y - 1) },
    { x, y -> Point2(x - 1, y + 1) })

data class Point2(val x: Int, val y: Int) : Comparable<Point2> {
    fun adjacent(): List<Point2> = adjDirs.map { it(x, y) }.sorted()

    fun diagAdjacent(): List<Point2> = (diagDirs.map { it(x, y) } + this.adjacent()).sorted()

    fun diagAdjacentWithSelf(): List<Point2> = (diagAdjacent() + this).sorted()

    operator fun plus(that: Point2): Point2 = (this.x + that.x) by (this.y + that.y)
    operator fun minus(that: Point2): Point2 = (this.x - that.x) by (this.y - that.y)

    override fun compareTo(other: Point2): Int {
        if (this.x > other.x) return 1
        if (this.x < other.x) return -1
        if (this.y > other.y) return 1
        if (this.y < other.y) return -1
        return 0
    }
}

infix fun Int.by(that: Int): Point2 = Point2(this, that)
