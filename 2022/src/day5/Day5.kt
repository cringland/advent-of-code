package day5

import Day
import java.io.File
import java.util.*

class Day5 : Day {
    private val input = File("src/day5/input").readLines().filter { it.isNotEmpty() }
            .map {
                it.split("move ", " from ", " to ").filter(String::isNotEmpty).map(String::toInt)
            }

    private fun testStacks(): List<Stack<Char>> {
        return listOf(
                stack("NZ"),
                stack("DCM"),
                stack("P")
        )
    }

    private fun stacks(): List<Stack<Char>> {
        return listOf(
                stack("MSJLVFNR"),
                stack("HWJFZDNP"),
                stack("GDCRW"),
                stack("SBN"),
                stack("NFBCPWZM"),
                stack("WMRP"),
                stack("WSLGNTR"),
                stack("VBNFHTQ"),
                stack("FNZHML")
        )
    }

    override fun problemOne(): String {
        val stacks = stacks()
        input.forEach { nums ->
            val from = nums[1] - 1
            val to = nums[2] - 1
            val amount = nums[0]
            for (i in 1..amount) {
                stacks[to].push(stacks[from].pop())
            }
        }
        return stacks.map { it.peek() }.joinToString(separator = "")
    }

    override fun problemTwo(): String {
        val stacks = stacks()
        input.forEach { nums ->
            val from = nums[1] - 1
            val to = nums[2] - 1
            val amount = nums[0]
            (1..amount).map {
                stacks[from].pop()
            }.reversed().forEach { item -> stacks[to].push(item) }
        }
        return stacks.map { it.peek() }.joinToString(separator = "")
    }

    private fun stack(word: String): Stack<Char> {
        val stack = Stack<Char>()
        word.reversed().forEach { stack.push(it) }
        return stack
    }
}
