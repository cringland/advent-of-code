package day8

import Day

class Day8 : Day {

    private val input = inputFile().readLines().map { it.map(Char::toString).map(String::toInt) }
    private val HEIGHT = input.size
    private val WIDTH = input[0].size

    override fun problemOne(): Int {
        val points = mutableSetOf<Pair<Int, Int>>()
        //y is rows / lines
        //x is columns

        //rows from left
        //
        for (y in 0 until HEIGHT) {
            val others = mutableListOf(input[y to 0])
            points.add(y to 0)
            for (x in 1 until WIDTH - 1) {
                if (input[y][x] > others.max() ?: 0) {
                    points.add(y to x)
                    others.add(input[y to x])
                }
            }
        }

        //rows from right
        for (y in 0 until HEIGHT) {
            val others = mutableListOf(input[y to WIDTH - 1])
            points.add(y to WIDTH - 1)
            for (x in WIDTH - 1 downTo 1) {
                if (input[y][x] > others.max() ?: 0) {
                    points.add(y to x)
                    others.add(input[y to x])
                }
            }
        }

        //cols from top
        for (x in 0 until WIDTH) {
            val others = mutableListOf(input[0 to x])
            points.add(0 to x)
            for (y in 1 until HEIGHT - 1) {
                val value = input[y to x]
                val max = others.max()
                val b = value > max ?: 0
                if (input[y][x] > others.max() ?: 0) {
                    points.add(y to x)
                    others.add(input[y to x])
                }
            }
        }


        //cols from bottom
        for (x in 0 until WIDTH) {
            val others = mutableListOf(input[HEIGHT - 1 to x])
            points.add(HEIGHT - 1 to x)
            for (y in HEIGHT - 1 downTo 1) {
                if (input[y to x] > others.max() ?: 0) {
                    points.add(y to x)
                    others.add(input[y][x])
                }
            }
        }
        return points.size
    }


    override fun problemTwo(): Number {
        var bestScore = 0
        for (x in 1 until WIDTH - 1) {
            for (y in 1 until HEIGHT - 1) {
                val current = input[y to x]
                val distances = mutableListOf<Int>()
                var currentDistance = 0
                //left
                for (x2 in x - 1 downTo 0) {
                    val other = input[y to x2]
                    currentDistance++
                    if (current <= other) {
                        break
                    }
                }
                distances.add(currentDistance)
                currentDistance = 0
                //right
                for (x2 in x + 1 until WIDTH) {
                    val other = input[y to x2]
                    currentDistance++
                    if (current <= other) {
                        break
                    }
                }
                distances.add(currentDistance)
                currentDistance = 0
                //up
                for (y2 in y - 1 downTo 0) {
                    val other = input[y2 to x]
                    currentDistance++
                    if (current <= other) {
                        break
                    }
                }
                distances.add(currentDistance)
                currentDistance = 0
                //up
                for (y2 in y + 1 until HEIGHT) {
                    val other = input[y2 to x]
                    currentDistance++
                    if (current <= other) {
                        break
                    }
                }
                distances.add(currentDistance)
                val score = distances.fold(1) { acc, it -> it * acc }
                if (bestScore < score)
                    bestScore = score
            }
        }
        return bestScore
    }

    operator fun List<List<Int>>.get(p: Pair<Int, Int>): Int {
        return this[p.first][p.second]

    }
}
