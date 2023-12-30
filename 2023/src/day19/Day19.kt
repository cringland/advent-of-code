package day19

import Day

fun String.findAllNums() = "(\\d+)".toRegex().findAll(this).map { it.value.toInt() }.toList()
fun IntRange.combine(that: IntRange): IntRange {
    val min = this.first.coerceAtLeast(that.first)
    val max = this.last.coerceAtMost(that.last)
    return min..max
}

class Day19 : Day {
    data class SplitRule(val range: IntRange, val partAttr: Char, val destination: String, val index: Int)
    data class Rule(private val line: String, val workflowName: String, val index: Int) {

        private val isGreaterThan = line.contains('>')
        private val isLesserThan = line.contains('<')
        private val partAttr = line.first()
        val noCondition = isGreaterThan == isLesserThan
        val number = if (!noCondition) line.findAllNums().first() else -1
        val destination = line.substringAfter(':')
        fun matches(part: Part): Boolean {
            if (!isGreaterThan && !isLesserThan) return true
            val partNumber = when (partAttr) {
                'x' -> part.x
                'm' -> part.m
                'a' -> part.a
                's' -> part.s
                else -> throw Exception()
            }
            return if (isGreaterThan) partNumber > number else partNumber < number
        }

        fun first(): SplitRule {
            if (noCondition) throw Exception()
            val gtRange = if (isGreaterThan) number + 1..4000 else number..4000
            val dest = if (isGreaterThan) this.destination else this.workflowName
            val index = if (isGreaterThan) 0 else this.index + 1
            return SplitRule(gtRange, this.partAttr, dest, index)
        }

        fun second(): SplitRule {
            if (noCondition) throw Exception()
            val ltRange = if (isLesserThan) 0 until number else 0..number
            val dest = if (isLesserThan) this.destination else this.workflowName
            val index = if (isLesserThan) 0 else this.index + 1
            return SplitRule(ltRange, this.partAttr, dest, index)
        }
    }

    data class Workflow(private val line: String) {
        val name = line.substringBefore('{')
        val rules = line.substringAfter('{').dropLast(1).split(",")
            .mapIndexed { i, string -> Rule(string, this.name, i) }
        fun solve(part: Part): String {
            rules.forEach { if (it.matches(part)) return it.destination }
            throw Exception()
        }
    }

    data class Part(private val nums: List<Int>) {
        val x = nums[0]
        val m = nums[1]
        val a = nums[2]
        val s = nums[3]
        val sum = nums.sum()
    }

    data class PartRange(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
        fun with(c: Char, range: IntRange) = when (c) {
            'x' -> PartRange(x.combine(range), m, a, s)
            'm' -> PartRange(x, m.combine(range), a, s)
            'a' -> PartRange(x, m, a.combine(range), s)
            's' -> PartRange(x, m, a, s.combine(range))
            else -> throw Exception()
        }

        fun size(): Long {
            fun IntRange.size() = (this.last - this.first + 1).toLong()
            return x.size() * m.size() * a.size() * s.size()
        }
    }

    private val input = inputFile().readText()
    private val workflows = input.substringBefore("\n\n").lines().map(::Workflow)
    private val workflowMap = workflows.associateBy { it.name }
    private val parts = input.substringAfter("\n\n").lines().map(String::findAllNums).map(::Part)

    override fun problemOne(): Number {
        return parts.filter { part ->
            val workflow = workflowMap["in"]!!
            var currentResult = workflow.solve(part)
            while (currentResult != "A" && currentResult != "R") {
                currentResult = workflowMap[currentResult]!!.solve(part)
            }
            currentResult == "A"
        }.map { it.sum }.sum()
    }

    private fun getRanges(currentRange: PartRange, currentRule: Rule): List<PartRange> {
        if (currentRule.noCondition && currentRule.destination == "A") return listOf(currentRange)
        if (currentRule.noCondition && currentRule.destination == "R") return emptyList()
        if (currentRule.noCondition) return getRanges(
            currentRange,
            workflowMap[currentRule.destination]!!.rules.first()
        )

        fun recurser(splitRule: SplitRule): List<PartRange> {
            val newRange = currentRange.with(splitRule.partAttr, splitRule.range)
            val nextRule by lazy { workflowMap[splitRule.destination]!!.rules[splitRule.index] }
            if (splitRule.destination == "R") return emptyList()
            if (splitRule.destination == "A") return listOf(newRange)
            return getRanges(newRange, nextRule)
        }
        return recurser(currentRule.first()) + recurser(currentRule.second())
    }

    override fun problemTwo(): Number {
        val startRange = 1..4000
        val startPartRange = PartRange(startRange, startRange, startRange, startRange)
        val workflow = workflowMap["in"]!!
        val ranges = getRanges(startPartRange, workflow.rules.first())
        return ranges.map { it.size() }.sum()
    }
}
