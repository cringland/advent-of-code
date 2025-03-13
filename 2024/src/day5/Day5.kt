package day5

import Day
import util.*

class Day5 : Day {
    private val input = inputFile().readText().split("\n\n")
    private val rules = input[0].lines().map { line ->
        val numbers = line.split("|").map { it.toInt() }
        numbers[0] to numbers[1]
    }

    private fun rulesMatch(list: List<Int>): Boolean =
        rules.all { rule ->
            val idxOne = list.indexOf(rule.first)
            val idxTwo = list.indexOf(rule.second)
            idxOne == -1 || idxTwo == -1 || idxOne < idxTwo
        }

    private val pages = input[1].lines().map { line ->
        line.split(",").map { it.toInt() }
    }
    private val valid = pages.filter { rulesMatch(it) }.toSet()
    private val invalid = pages.filter { it !in valid }.toSet()

    override fun problemOne(): Number {
        assert((valid.size + invalid.size) == pages.size)
        return valid.map { it[(it.size - 1) / 2] }.sum()
    }

    override fun problemTwo(): Number {
        val temp = invalid.map { page ->
            val newPage = page.toMutableList()
            while (!rulesMatch(newPage)) {
                rules.forEach { rule ->
                    val idxOne = newPage.indexOf(rule.first)
                    val idxTwo = newPage.indexOf(rule.second)
                    if (idxOne != -1 && idxTwo != -1 && idxOne > idxTwo) {
                        newPage[idxOne]= rule.second
                        newPage[idxTwo] = rule.first
                    }
                }
            }
            newPage
        }
        return temp.map { it[(it.size - 1) / 2] }.sum()
    }
}
