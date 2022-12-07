package day4

import Day

class Day4 : Day {
    private val input = inputFile().readLines()
    private val numbers = input[0].split(",").map { Integer.valueOf(it) }
    private val boards = input.drop(1)
            .filter { it.isNotEmpty() }
            .chunked(5)
            .map {
                it.map { str ->
                    str.trim().split(" ")
                            .filter(String::isNotEmpty)
                            .map { Integer.valueOf(it) to false }
                }
            }

    override fun problemOne(): Int {
        var markedBoards = boards
        numbers.forEach { calledNum ->
            markedBoards = markedBoards.markNumbers(calledNum)
            val winner = markedBoards.find { board -> board.hasBingo() }.orEmpty()
            if (winner.isNotEmpty())
                return sumUnmarked(winner) * calledNum
        }
        throw Exception("Somethings not worked")
    }

    override fun problemTwo(): Int {
        var markedBoards = boards
        numbers.forEach { calledNum ->
            markedBoards = markedBoards.markNumbers(calledNum)
            if (markedBoards.size > 1) {
                markedBoards = markedBoards.filter { board -> !board.hasBingo() }
            } else if (markedBoards[0].hasBingo()) {
                return sumUnmarked(markedBoards[0]) * calledNum
            }
        }
        throw Exception("Somethings not worked")
    }

    private fun List<List<List<Pair<Int, Boolean>>>>.markNumbers(calledNum: Int?): List<List<List<Pair<Int, Boolean>>>> {
        return map {
            it.map {
                it.map { if (it.first == calledNum) it.copy(second = true) else it }
            }
        }
    }

    private fun sumUnmarked(board: List<List<Pair<Int, Boolean>>>): Int {
        return board.fold(0) { total, row -> total + row.fold(0) { rowTotal, value -> if (!value.second) rowTotal + value.first else rowTotal } }
    }

    private fun List<List<Pair<Int, Boolean>>>.hasBingo(): Boolean {
        val rowMatch = any { it.all { it.second } }
        val columnMatch = mapIndexed { i, _ -> map { it[i] } }.any { it.all { it.second } }
        return rowMatch || columnMatch
    }


}
