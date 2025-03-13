package day11

import Day

class Day11 : Day {

    private val input = inputFile().readText().split(" ").map { it.toLong() }

    override fun problemOne(): Number {
        var stones = input
        fun List<Long>.blink(): List<Long> {
            return this.flatMap { stone ->
                if (stone == 0L) listOf(1L)
                else {
                    val asString = stone.toString()
                    val l = asString.length
                    if (l % 2 == 0) {
                        listOf(
                            asString.substring(0, (l / 2)).toLong(),
                            asString.substring(l / 2).toLong()
                        )
                    } else {
                        listOf(stone * 2024L)
                    }
                }
            }
        }
        repeat(25) {
            stones = stones.blink()
        }
        return stones.size
    }

    override fun problemTwo(): Number {
        val map: MutableMap<Long, List<Long>> = mutableMapOf()
        val map2: MutableMap<Pair<Long, Int>, Long> = mutableMapOf()

        fun Long.oneBlink(): List<Long> {
            return if (this == 0L) listOf(1L)
            else {
                val asString = this.toString()
                val l = asString.length
                if (l % 2 == 0) {
                    listOf(
                        asString.substring(0, (l / 2)).toLong(),
                        asString.substring(l / 2).toLong()
                    )
                } else {
                    listOf(this * 2024L)
                }
            }
        }

        fun Long.fifteenBlink(): List<Long> {
            return map[this] ?: (0..14).fold(listOf(this)) { list, _ ->
                list.flatMap { it.oneBlink() }
            }.also { map[this] = it }
        }

        fun Long.blink(i: Int): Long {
            if (map2.contains(this to i)) {
                return map2[this to i]!!
            }
            if (i == 5) return 1
            return this.fifteenBlink().map { it.blink(i+1) }.sum().also { map2[this to i] = it }
        }
//        264350935776416
//        500143441615 too low
        return input.map { it.blink(0) }.sum()
    }
}
