package day18

import Day
import java.io.File
import kotlin.test.assertEquals

class Day18 : Day {

    private val input = File("src/day18/input").readLines().filter(String::isNotEmpty)

    override fun problemOne(): Int {
        var currentStr = input.first()
        for (i in 1 until input.size) {
            currentStr = currentStr.add(input[i]).reduced()
        }
        return currentStr.magnitude()
    }

    private fun tests() {
        assertEquals("[[[[0,9],2],3],4]", explode("[[[[[9,8],1],2],3],4]"))
        assertEquals("[7,[6,[5,[7,0]]]]", explode("[7,[6,[5,[4,[3,2]]]]]"))
        assertEquals("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", explode("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"))
        assertEquals("[[3,[2,[8,0]]],[9,[5,[7,0]]]]", explode("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"))
        assertEquals("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", "[[[[4,3],4],4],[7,[[8,4],9]]]".add("[1,1]").reduced())
    }

    private fun String.reduced(): String {
        val newStr = this.reduce()
        return if (newStr == this) this else newStr.reduced()
    }

    private fun String.reduce(): String {
        val newStr = explode(this)
        return if (newStr == this) splitt(this) else newStr
    }

    private fun explode(str: String): String {
        var depth = 0
        for ((index, c) in str.withIndex()) {
            if (c == '[') depth++ else if (c == ']') depth--
            if (depth == 5) {
                val explodee = str.substring(index, str.indexOf(']', index) + 1)
                var previousStr = str.substring(0, index)
                var postStr = str.substring(str.indexOf(']', index) + 1)

                if (previousStr.last() == ',') {
                    val prevNumMatch = previousStr.lastNumMatch()!!
                    val newPrevNum = prevNumMatch.value.toInt() + explodee.firstNumMatch()!!.value.toInt()
                    previousStr = previousStr.replaceRange(prevNumMatch.range, newPrevNum.toString())

                    val nextNumMatch = postStr.firstNumMatch()
                    if (nextNumMatch != null) {
                        val nextNum = nextNumMatch.value.toInt() + explodee.lastNumMatch()!!.value.toInt()
                        postStr = postStr.replaceRange(nextNumMatch.range, nextNum.toString())
                    }
//                    println("after explode: ${previousStr + "0" + postStr}")
                    return previousStr + "0" + postStr
                } else {
                    val nextNumMatch = postStr.firstNumMatch()!!
                    val nextNum = nextNumMatch.value.toInt() + explodee.lastNumMatch()!!.value.toInt()
                    postStr = postStr.replaceRange(nextNumMatch.range, nextNum.toString())
                    val prevNumMatch = previousStr.lastNumMatch()
                    if (prevNumMatch != null) {
                        val newPrevNum = prevNumMatch.value.toInt() + explodee.firstNumMatch()!!.value.toInt()
                        previousStr = previousStr.replaceRange(prevNumMatch.range, newPrevNum.toString())
                    }
//                    println("after explode: ${previousStr + "0" + postStr}")
                    return previousStr + "0" + postStr
                }
            }
        }
        return str
    }

    private fun String.magnitude(): Int {
        var newStr = this
        while (newStr.contains("[")) {
            newStr = newStr.replace("(\\[\\d*,\\d*\\])".toRegex()) {
                val (x, y) = it.value.replace("[", "").replace("]", "").split(",")
                ((3 * x.toInt()) + (2 * y.toInt())).toString()
            }
        }
        return newStr.toInt()
    }

    private fun splitt(str: String): String {
        val match = str.firstDoubleDigit()
        if (match == null) return str else {
            val newNum = match.value.toDouble() / 2
            val split = "[${newNum.toInt()},${Math.ceil(newNum).toInt()}]"
//            println("after split: ${str.replaceRange(match.range, split)}")
            return str.replaceRange(match.range, split)
        }
    }

    private fun String.add(that: String): String {
//        println("after addition: [$this,$that]")
        return "[$this,$that]"
    }

    private fun String.lastNumMatch(): MatchResult? {
        return "(\\d+)(?!.*\\d)".toRegex().find(this)
    }

    private fun String.firstNumMatch(): MatchResult? {
        return "(\\d+)".toRegex().find(this)
    }

    private fun String.firstDoubleDigit(): MatchResult? {
        return "(\\d{2})".toRegex().find(this)
    }

    override fun problemTwo(): Int {
        var largest = 0
        for (i in 0 until input.size) {
            for (y in 0 until input.size) {
                if (i != y) {
                    val current = input[i].add(input[y]).reduced().magnitude()
                    if (largest < current) largest = current
                }
            }
        }
        return largest
    }
}

