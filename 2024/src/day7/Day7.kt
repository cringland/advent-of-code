package day7

import Day
import util.Point2
import util.by

class Day7 : Day {
    private val input = inputFile().readLines().map {line ->
        line.filterNot { it == ':' }.split(" ").let { it ->
            it[0].toLong() to it.drop(1).map(String::toLong)
        }
    }

    private val p1Operators = listOf<(Long,Long) -> Long>({ one, two -> one + two }, { one, two -> one * two })
    private val p2Operators = p1Operators + { one, two -> (one.toString() + two.toString()).toLong() }
    private fun solve(target: Long, current: Long, remainingList: List<Long>, operators: List<(Long, Long) -> Long>): Boolean {
        if (remainingList.isEmpty()) return current == target

        return operators.any{
            solve(target, it(current, remainingList.first()), remainingList.drop(1), operators)
        }
    }

    private val p1Valid = input.filter {
        solve(it.first, it.second.first(), it.second.drop(1), p1Operators)
    }
    val p1Answer = p1Valid.map { it.first }.sum()
    private val p1Invalid = input.filter { it !in p1Valid }

    override fun problemOne(): Number {
        return p1Answer
    }

    override fun problemTwo(): Number {
        return p1Answer + p1Invalid.filter {
            solve(it.first, it.second.first(), it.second.drop(1), p2Operators)
        }.map { it.first }.sum()
    }


}
