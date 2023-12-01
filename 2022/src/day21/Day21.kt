package day21

import Day

class Day21 : Day {
    private fun String.op() = this[5]
    private val ops = mapOf<Char, (Long, Long) -> Long>(
        '+' to { a, b -> a + b },
        '*' to { a, b -> a * b },
        '-' to { a, b -> a - b },
        '/' to { a, b -> a / b }
    )

    private fun calc(op: Char, v1: Long, v2: Long) = ops[op]!!(v1, v2)

    private val input = inputFile().readLines().associate {
        it.split(": ").let { list -> list[0] to list[1] }
    }

    private val input2 = input.toMutableMap().apply {
        this["root"] = this["root"]!!.replaceRange(4..6, " = ")
        this["humn"] = "~"
    }.toMap()

    override fun problemOne(): Number = solve("root")

    private fun solve(key: String): Long {
        val value = input[key]!!
        return if (value[0].isDigit())
            value.toLong()
        else calc(value.op(), solve(value.take(4)), solve(value.takeLast(4)))
    }

    override fun problemTwo(): String = solve2("root").replace("~", "x") //Solve for x :(

    private fun solve2(key: String): String {
        val value = input2[key]!!
        if (value[0].isDigit() || value[0] == '~')
            return value
        val v1 = solve2(value.take(4))
        val v2 = solve2(value.takeLast(4))
        return if (v1[0].isDigit() && v2[0].isDigit()) {
            calc(value.op(), v1.toLong(), v2.toLong()).toString()
        } else "(" + v1 + value.substring(4..6) + v2 + ")" //TODO Auto solve this
    }
}
