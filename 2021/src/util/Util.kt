package util

val adjDirs = listOf<(Int, Int) -> Point2>(
        { x, y -> Point2(x - 1, y) },
        { x, y -> Point2(x + 1, y) },
        { x, y -> Point2(x, y - 1) },
        { x, y -> Point2(x, y + 1) })

val diagDirs = listOf<(Int, Int) -> Point2>(
        { x, y -> Point2(x - 1, y - 1) },
        { x, y -> Point2(x + 1, y + 1) },
        { x, y -> Point2(x + 1, y - 1) },
        { x, y -> Point2(x - 1, y + 1) })

data class Point2(val x: Int, val y: Int) {
    fun adjacent(): List<Point2> = adjDirs.map { it(x, y) }

    fun diagAdjacent(): List<Point2> = diagDirs.map { it(x, y) } + this.adjacent()

}

infix fun Int.by(that: Int): Point2 = Point2(this, that)

