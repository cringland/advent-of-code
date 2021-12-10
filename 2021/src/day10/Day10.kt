package day10

import Day
import java.io.File
import java.util.*

class Day10 : Day {
    private val closeMap = mapOf(
            '(' to ')',
            '[' to ']',
            '{' to '}',
            '<' to '>')

    private val input = File("src/day10/input").readLines().map(String::toCharArray)


    override fun problemOne(): Int {
        return input.mapNotNull { line -> corruptedCheck(line) }.map {
            when (it.second) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                else -> 25137
            }
        }.sum()
    }

    override fun problemTwo(): Long {
        val scores = input.mapNotNull { line -> autocomplete(line) }
                .map {
                    it.fold(0L) { acc, c ->
                        val i = when (c) {
                            ')' -> 1
                            ']' -> 2
                            '}' -> 3
                            else -> 4
                        }
                        (acc * 5) + i
                    }
                }.sorted()
        val mid = (scores.size - 1) / 2
        return scores[mid]
    }

    private fun corruptedCheck(line: CharArray): Pair<CharArray, Char>? {
        val expectedClosingStack = Stack<Char>()
        for (c in line) {
            when {
                closeMap.keys.contains(c) -> expectedClosingStack.push(closeMap[c])
                expectedClosingStack.peek() == c -> expectedClosingStack.pop()
                else -> return line to c
            }
        }
        return null
    }

    private fun autocomplete(line: CharArray): CharArray? {
        val expectedClosingStack = Stack<Char>()
        for (c in line) {
            when {
                closeMap.keys.contains(c) -> expectedClosingStack.push(closeMap[c])
                expectedClosingStack.peek() == c -> expectedClosingStack.pop()
                else -> return null
            }
        }
        return expectedClosingStack.toCharArray().reversedArray()
    }
}
