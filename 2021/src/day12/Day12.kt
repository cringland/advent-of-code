package day12

import Day
import java.io.File

private fun String.isSmall(): Boolean {
    return this.any { it.isLowerCase() }
}

class Day12 : Day {
    private val caveConnections: List<Pair<String, String>> = inputFile().readLines()
            .flatMap {
                val split = it.split("-")
                when {
                    "start" in split -> {
                        val cave = split.find { it != "start" }!!
                        listOf("start" to cave)
                    }
                    "end" in split -> {
                        val cave = split.find { it != "end" }!!
                        listOf(cave to "end")
                    }
                    else -> {
                        val (a, b) = split
                        listOf(a to b, b to a)
                    }
                }
            }

    override fun problemOne(): Int {
        return allPaths(false).size
    }

    override fun problemTwo(): Int {
        return allPaths(true).size
    }
    
    private fun allPaths(canReuse: Boolean): Set<List<String>> {
        val paths: MutableSet<List<String>> = caveConnections.filter { it.first == "start" }
                .fold(mutableSetOf()) { acc, _ -> findPaths(acc, mutableListOf("start"), canReuse); acc }
        return paths.toSet()
    }

    private fun findPaths(paths: MutableSet<List<String>>, currentPath: MutableList<String>, canReuse: Boolean) {
        caveConnections.forEach { connection ->
            if (connection.first == currentPath.last()) {
                val second = connection.second
                val isEnd = second == "end"
                val repeated = second in currentPath

                if (isEnd) {
                    val copy = currentPath.toMutableList()
                    paths.add(copy + second)
                } else if (second.isSmall() && repeated && canReuse) {
                    val copy = currentPath.toMutableList()
                    copy.add(second)
                    findPaths(paths, copy, false)
                } else if ((connection.second.isSmall() && !repeated) || !second.isSmall()) {
                    val copy = currentPath.toMutableList()
                    copy.add(second)
                    findPaths(paths, copy, canReuse)
                }
            }
        }
    }
}


