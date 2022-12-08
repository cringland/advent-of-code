package day5

import Day
import java.util.*

class Day5 : Day {

    data class Rule(val amount: Int, val from: Int, val to: Int)

    private val input = inputFile().readText().split("\n\n")

    private fun stacks(): List<Stack<Char>> {
        val lines = input[0].lines().takeWhile { !it.contains('1') }
        val cols = lines.last().count { it == '[' }
        val words = (0 until cols).map { x ->
            lines.fold("") { acc, str ->
                acc + str[1 + (x * 4)]
            }.replace(" ", "")
        }
        return words.map { stack(it) }
    }

    private val rules = input[1].lines().filter(String::isNotEmpty)
            .map {
                val nums = it.split("move ", " from ", " to ").filter(String::isNotEmpty).map(String::toInt)
                Rule(nums[0], nums[1] - 1, nums[2] - 1)
            }

    override fun problemOne(): String {
        val stacks = stacks()
        rules.forEach { rule ->
            repeat(rule.amount) {
                stacks[rule.to].push(stacks[rule.from].pop())
            }
        }
        return stacks.joinFirst()
    }

    override fun problemTwo(): String {
        val stacks = stacks()
        rules.forEach { rule ->
            (1..rule.amount).map {
                stacks[rule.from].pop()
            }.reversed().forEach { item -> stacks[rule.to].push(item) }
        }
        return stacks.joinFirst()
    }

    private fun List<Stack<Char>>.joinFirst(): String = this.map { it.peek() }.joinToString(separator = "")

    private fun stack(word: String): Stack<Char> {
        val stack = Stack<Char>()
        word.reversed().forEach { stack.push(it) }
        return stack
    }
}
