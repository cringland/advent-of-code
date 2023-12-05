package day5

import Day
import java.time.LocalTime
import java.util.*

class Day5 : Day {
    data class Converter(val destRangeStart: Long, val sourceRageStart: Long, val rangeLength: Long) {
        fun canConvert(start: Long) = (sourceRageStart until (sourceRageStart + rangeLength)).contains(start)
        fun convert(start: Long) = start - sourceRageStart + destRangeStart
    }

    private val seeds = inputFile().readLines().first()
        .substringAfter(": ")
        .split(" ")
        .map { it.toLong() }

    private val maps = inputFile().readText().substringAfter("\n\n")
        .split("\n\n")
        .map { block ->
            val lines = block.lines()
            lines.drop(1).map {
                val nums = it.split(" ")
                Converter(nums[0].toLong(), nums[1].toLong(), nums[2].toLong())
            }
        }

    override fun problemOne(): Number {
        val mappedSeeds = seeds.map { seed ->
            maps.fold(seed) { num, converters ->
                converters.find { converter -> converter.canConvert(num) }?.convert(num) ?: num
            }
        }
        return mappedSeeds.min()!!
    }

    override fun problemTwo(): Number {
        val ranges = seeds.chunked(2)
            .map { it[0]..(it[0] + it[1]) }
        val mappedSeeds = ranges.mapIndexed { i, range ->
            println("${LocalTime.now()}: Onto range $i")
            range.fold(Long.MAX_VALUE) { currentMin, seed ->
                val prevRanges = ranges.subList(0, i)
                val current = maps.fold(seed) { num, converters ->
                    if (prevRanges.none { seed in it })
                        converters.find { converter -> converter.canConvert(num) }?.convert(num) ?: num
                    else Long.MAX_VALUE
                }
                current.coerceAtMost(currentMin)
            }
        }
        //Takes 20 minutes. Could maybe be improved a bit
        return mappedSeeds.min()!!
    }
}
