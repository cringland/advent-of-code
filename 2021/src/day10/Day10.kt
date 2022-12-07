package day10

import Day
import java.io.File
import java.util.*

class Day10 : Day {
    private val lines = inputFile().readLines().map { Line(it) }


    override fun problemOne(): Int {
        return lines.mapNotNull { it.corruptLetter }.map {
            when (it) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                else -> 25137
            }
        }.sum()
    }

    override fun problemTwo(): Long {
        val scores = lines.mapNotNull { it.autocomplete }
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
        return scores[(scores.size - 1) / 2]
    }
}

class Line(str: String) {

    private val closeMap = mapOf(
            '(' to ')',
            '[' to ']',
            '{' to '}',
            '<' to '>')

    var corruptLetter: Char? = null
    var autocomplete: CharArray? = null

    init {
        val expectedClosingStack = Stack<Char>()
        loop@ for (c in str) {
            when (c) {
                in closeMap -> expectedClosingStack.push(closeMap[c])
                expectedClosingStack.peek() -> expectedClosingStack.pop()
                else -> {
                    corruptLetter = c
                    break@loop
                }
            }
        }
        if (corruptLetter == null)
            autocomplete = expectedClosingStack.toCharArray().reversedArray()
    }
}
