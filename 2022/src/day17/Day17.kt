package day17

import Day
import util.Point2
import util.by


class Day17 : Day {
    private val input = inputFile().readText()

    class Directions(private val str: String) {
        private var count = 0
        fun next(): Int = str[count % str.length].let { if (it == '<') -1 else 1 }.also { count++ }
    }

    class Rock {
        private val rocks = listOf(
            listOf(0 by 0, 1 by 0, 2 by 0, 3 by 0),
            listOf(1 by 0, 0 by 1, 1 by 1, 2 by 1, 1 by 2),
            listOf(0 by 0, 1 by 0, 2 by 0, 2 by 1, 2 by 2),
            listOf(0 by 0, 0 by 1, 0 by 2, 0 by 3),
            listOf(0 by 0, 0 by 1, 1 by 0, 1 by 1)
        )
        private var count = 0
        fun next(): List<Point2> = rocks[count % rocks.size].apply { count++ }
    }

    private val maxHeights = run {
        val rocks = Rock()
        val dirs = Directions(input)
        val maxHeights = mutableListOf(0)
        val points = mutableSetOf(0 by 0, 1 by 0, 2 by 0, 3 by 0, 4 by 0, 5 by 0, 6 by 0)
        for (i in 1..5000) {
            val pointPlus = 2 by (points.map { it.y }.max()!! + 4)
            var rock = rocks.next().map { it + pointPlus }
            while (true) {
                val dir = dirs.next()
                val shouldNotMove = rock.any {
                    val newPos = (it.x + dir) by it.y
                    newPos.x > 6 || newPos.x < 0 || newPos in points
                }
                rock = if (!shouldNotMove) rock.map { (it.x + dir) by it.y } else rock

                val shouldStop = rock.any {
                    val newPos = it.x by it.y - 1
                    newPos in points
                }

                if (shouldStop) {
                    points.addAll(rock)
                    break
                }
                rock = rock.map { it.x by it.y - 1 }
            }
            maxHeights.add(points.map { it.y }.max()!!)
        }
        maxHeights.toList()
    }

    override fun problemOne(): Number {
        return maxHeights[2022]
    }

    override fun problemTwo(): Number {
        val diffs = maxHeights.dropLast(1).mapIndexed { i, it -> maxHeights[i + 1] - it }
        //Regex stolen from here https://stackoverflow.com/questions/29438282/find-repeated-pattern-in-a-string-of-characters-using-r
        val repeatingRegex = "\\b(\\S+?)\\1\\S*\\b".toRegex()
        val desiredLength = 1000000000000
        val initialBuffer = 100

        val diffsString = diffs.drop(initialBuffer).joinToString(separator = "")
        val repeatingSeq = repeatingRegex.find(diffsString)!!.groupValues.last().map { it.toString().toLong() }
        val seqLength = repeatingSeq.size
        val repeatingSum = repeatingSeq.sum()
        val bufferSum = diffs.take(initialBuffer).sum()
        val remainderAmt = ((desiredLength - initialBuffer) % seqLength).toInt()
        val remainderSum = repeatingSeq.take(remainderAmt).sum()
        return bufferSum + (((desiredLength - initialBuffer) / seqLength) * repeatingSum) + remainderSum
    }

    private fun Set<Point2>.draw() {
        val minX = 0
        val minY = 0
        val maxX = 6
        val maxY = this.map { it.y }.max()!!
        println()
        (maxY downTo minY).forEach { y ->
            (minX..maxX).forEach { x ->
                print(if (x by y in this) '#' else '.')
            }
            println()
        }
        println()
    }
}
