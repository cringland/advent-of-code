package util


val diagPts = listOf(
    Point2(- 1, - 1),
    Point2(+ 1, + 1),
    Point2(+ 1, - 1),
    Point2(- 1, + 1))

val up = Point2(0, -1)
val down = Point2(0, 1)
val left = Point2(-1, 0)
val right = Point2(1, 0)

val cardinalPts = listOf(up, down, left, right)


data class Point2(val x: Int, val y: Int) : Comparable<Point2> {

    operator fun plus(that: Point2): Point2 = (this.x + that.x) by (this.y + that.y)
    operator fun minus(that: Point2): Point2 = (this.x - that.x) by (this.y - that.y)
    fun adjacent(): Set<Point2> = setOf(this + up, this + right, this + down, this + left)

    override fun compareTo(other: Point2): Int {
        if (this.x > other.x) return 1
        if (this.x < other.x) return -1
        if (this.y > other.y) return 1
        if (this.y < other.y) return -1
        return 0
    }
}

infix fun Int.by(that: Int): Point2 = Point2(this, that)
