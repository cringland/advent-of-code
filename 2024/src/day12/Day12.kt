package day12

import Day
import util.Point2
import util.by

class Day12 : Day {

    private val input = inputFile().readLines()
    private val charGroups = input.mapIndexed { y, row ->
        row.mapIndexed { x, c ->
            c to (x by y)
        }
    }.flatten().groupBy {it.first}.mapValues { entry -> entry.value.map { it.second }.toSet() }
    private val regions = charGroups.flatMap { entry ->
        val regions: MutableList<Pair<Char, List<Point2>>> = mutableListOf()
        val char = entry.key
        val mutableValue = entry.value.toMutableList()
        var currentRegion: MutableList<Point2> = mutableListOf()
        while (mutableValue.isNotEmpty()) {
            if (currentRegion.isEmpty()) {
                currentRegion.add(mutableValue.removeAt(0))
            }
            val pts = mutableValue.filter { pt -> pt.adjacent().any { adj -> adj in currentRegion } }
            if (pts.isEmpty()) {
                regions.add(char to currentRegion)
                currentRegion = mutableListOf()
            } else {
                mutableValue.removeAll(pts)
                currentRegion.addAll(pts)
            }
        }
        if (currentRegion.isNotEmpty()) regions.add(char to currentRegion)
        regions
    }

    override fun problemOne(): Number {
        return regions.map {entry ->
            val area = entry.second.size.toLong()
            val perimeter = entry.second.map { pt ->
                pt.adjacent().count { it !in entry.second }
            }.sum()
            area * perimeter
        }.sum()
    }

    override fun problemTwo(): Number {
        return regions.map {entry ->
            val area = entry.second.size.toLong()
            val ys = entry.second.map { it.y }.toSet()
            val xs = entry.second.map { it.x }.toSet()
            val horizonalLines = ys.flatMap {y ->
                val pts = entry.second.filter { it.y == y }.sortedBy { it.x }.toMutableList()
                val lines: MutableList<MutableList<Point2>> = mutableListOf(mutableListOf(pts.first()))
                pts.drop(1).forEach {
                    if (lines.last().last().x == it.x - 1) {
                        lines.last().add(it)
                    } else {
                        lines.add(mutableListOf(it))
                    }
                }
                lines
            }
            val verticalLines = xs.flatMap { x ->
                val pts = entry.second.filter { it.x == x }.sortedBy { it.y }.toMutableList()
                val lines: MutableList<MutableList<Point2>> = mutableListOf(mutableListOf(pts.first()))
                pts.drop(1).forEach {
                    if (lines.last().last().y == it.y - 1) {
                        lines.last().add(it)
                    } else {
                        lines.add(mutableListOf(it))
                    }
                }
                lines
            }
            val filteredVertical = verticalLines.map {
                it.filterNot { pt -> horizonalLines.any { line2 -> line2.any { pt2 -> pt == pt2 } } }
            }.filter { it.isNotEmpty() }
            val filteredHorizontal = horizonalLines.map {
                it.filterNot { pt -> verticalLines.any { line2 -> line2.any { pt2 -> pt == pt2 } } }
            }.filter { it.isNotEmpty() }

            val perimeter = filteredHorizontal.size + filteredVertical.size + 3
            area * perimeter
        }.sum()
    }
}
