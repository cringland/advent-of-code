package util

val adjDirs = mapOf<Dir, (Int, Int) -> Point2>(
    Dir.R to { x, y -> Point2(x + 1, y) },
    Dir.D to { x, y -> Point2(x, y + 1) },
    Dir.U to { x, y -> Point2(x, y - 1) },
    Dir.L to { x, y -> Point2(x - 1, y) })

val diagDirs = listOf<(Int, Int) -> Point2>(
    { x, y -> Point2(x - 1, y - 1) },
    { x, y -> Point2(x + 1, y + 1) },
    { x, y -> Point2(x + 1, y - 1) },
    { x, y -> Point2(x - 1, y + 1) })

val up = Point2(0, -1)
val down = Point2(0, 1)
val left = Point2(-1, 0)
val right = Point2(1, 0)

enum class Dir { L, R, D, U }

data class Point2(val x: Int, val y: Int) : Comparable<Point2> {
    fun adjacentWithDir(): List<Pair<Dir, Point2>> = adjDirs.map { it.key to it.value(x, y) }
    fun adjacent(): List<Point2> = adjDirs.values.map { it(x, y) }.sorted()

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
