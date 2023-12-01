package day22

import Day
import util.Point2
import util.by

class Day22 : Day {

    enum class Dir(val char: Char) {
        N('^'), E('>'), S('v'), W('<');

        fun turn(s: String): Dir = when (this) {
            N -> if (s == "R") E else W
            E -> if (s == "R") S else N
            S -> if (s == "R") W else E
            else -> if (s == "R") N else S
        }
    }

    private val input = inputFile().readLines()
    private val map = input.takeWhile(String::isNotEmpty)
    private val instructions = Regex("(\\d+|R|L)").findAll(input.last()).map { it.value }

    override fun problemOne(): Number {
        var pos = map.first().indexOfFirst { it == '.' } by 0
        var dir = Dir.E
        val temp = map.toMutableList()
        instructions.forEach { ins ->
            if (ins == "R" || ins == "L") {
                dir = dir.turn(ins)
            } else {
                for (i in 1..ins.toInt()) {
                    val newPos = when (dir) {
                        Dir.E -> {
                            val y = pos.y
                            var newX = pos.x + 1
                            if (newX >= map[y].length || map[pos.y][newX] == ' ') {
                                newX = map[y].indexOfFirst { it != ' ' }
                            }
                            newX by pos.y
                        }
                        Dir.W -> {
                            val y = pos.y
                            var newX = pos.x - 1
                            if (newX < 0 || map[pos.y][newX] == ' ') {
                                newX = map[y].indexOfLast { it != ' ' }
                            }
                            newX by pos.y
                        }
                        Dir.S -> {
                            val x = pos.x
                            var newY = pos.y + 1
                            if (newY >= map.size || map[newY].getOrElse(x) { ' ' } == ' ') {
                                newY = map.indexOfFirst { it[x] != ' ' }
                            }
                            x by newY
                        }
                        else -> {
                            val x = pos.x
                            var newY = pos.y - 1
                            if (newY < 0 || map[newY][pos.x] == ' ') {
                                newY = map.indexOfLast { it.getOrElse(x) { ' ' } != ' ' }
                            }
                            x by newY
                        }
                    }
                    temp[pos.y] = temp[pos.y].replaceRange(pos.x, pos.x + 1, dir.char.toString())
                    if (map[newPos.y][newPos.x] == '#') break
                    pos = newPos
                }
            }
        }
        return password(pos, dir)
    }

    override fun problemTwo(): Number {
        var pos = map.first().indexOfFirst { it == '.' } by 0
        var dir = Dir.E
        val temp = map.toMutableList()
        instructions.forEach { ins ->
            if (ins == "R" || ins == "L") {
                dir = dir.turn(ins)
            } else {
                for (i in 1..ins.toInt()) {
                    val newPos = when (dir) {
                        Dir.E -> {
                            val y = pos.y
                            var newX = pos.x + 1
                            if (newX >= map[y].length || map[pos.y][newX] == ' ') {
                                newX = map[y].indexOfFirst { it != ' ' }
                            }
                            newX by pos.y
                        }
                        Dir.W -> {
                            val y = pos.y
                            var newX = pos.x - 1
                            if (newX < 0 || map[pos.y][newX] == ' ') {
                                newX = map[y].indexOfLast { it != ' ' }
                            }
                            newX by pos.y
                        }
                        Dir.S -> {
                            val x = pos.x
                            var newY = pos.y + 1
                            if (newY >= map.size || map[newY].getOrElse(x, { ' ' }) == ' ') {
                                newY = map.indexOfFirst { it[x] != ' ' }
                            }
                            x by newY
                        }
                        else -> {
                            val x = pos.x
                            var newY = pos.y - 1
                            if (newY < 0 || map[newY][pos.x] == ' ') {
                                newY = map.indexOfLast { it.getOrElse(x, { ' ' }) != ' ' }
                            }
                            x by newY
                        }
                    }
                    temp[pos.y] = temp[pos.y].replaceRange(pos.x, pos.x + 1, dir.char.toString())
                    if (map[newPos.y][newPos.x] == '#') break
                    pos = newPos
                }
            }
        }
        return password(pos, dir)
    }

    private fun List<String>.print() = this.forEach { println(it) }

    private fun password(pos: Point2, dir: Dir) = (1000 * (pos.y + 1)) + (4 * (pos.x + 1)) + when (dir) {
        Dir.N -> 3
        Dir.E -> 0
        Dir.S -> 1
        else -> 2
    }
}
 
