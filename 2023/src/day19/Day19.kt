package day19

import Day

fun String.findAllNums() = "(\\d+)".toRegex().findAll(this).map { it.value.toLong() }.toList()
class Day19 : Day {
    data class Rule(private val line: String) {

        private val isGreaterThan = line.contains('>')
        private val isLesserThan = line.contains('<')
        private val partAttr = line.first()
        val number = if (isGreaterThan || isLesserThan) line.findAllNums().first() else -1
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
    }

    data class Workflow(private val line: String) {
        val name = line.substringBefore('{')
        val rules = line.substringAfter('{').dropLast(1).split(",").map(::Rule)
        fun solve(part: Part): String {
            rules.forEach { if (it.matches(part)) return it.destination }
            throw Exception()
        }
    }

    data class Part(private val nums: List<Long>) {
        val x = nums[0]
        val m = nums[1]
        val a = nums[2]
        val s = nums[3]
        val sum = nums.sum()
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

    override fun problemTwo(): Number {
//        val workflow = workflowMap["in"]!!
//        workflow.rules.first()
        return 1
    }
}
