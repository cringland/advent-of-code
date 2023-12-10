package day10

import Day
import util.Point2
import util.by
import kotlin.math.ceil

class Day10 : Day {
    private val input = inputFile().readLines()
    private val start = input.indexOfFirst { it.contains("S") }.let { y ->
        val x = input[y].indexOf("S")
        x by y
    }

    private val firstInLoop = start.adjacent().filter {
        if (it.y !in input.indices || it.x !in input[it.y].indices) false else {
            val nVal = input[it.y][it.x]
            (it.y > start.y && (nVal == 'J' || nVal == 'L' || nVal == '|'))
                    || (it.y < start.y && (nVal == '7' || nVal == 'F' || nVal == '|'))
                    || (it.x > start.x && (nVal == '7' || nVal == 'J' || nVal == '-'))
                    || (it.x < start.x && (nVal == 'L' || nVal == 'F' || nVal == '-'))
        }
    }

    private val loop = let {
//        val loop = mutableMapOf(start to 0, firstInLoop[0] to 1, firstInLoop[1] to 1)
        val loop = mutableMapOf(start to 0, firstInLoop[0] to 1)
//        val currentPoints = firstInLoop.toMutableList()
        val currentPoints = firstInLoop.take(1).toMutableList()
        while (currentPoints.isNotEmpty()) {
            val p = currentPoints[0]
            currentPoints.removeAt(0)
            val pVal = input[p.y][p.x]
            val dist = loop[p]!! + 1
            val connectingPipes = when (pVal) {
                '|' -> listOf(0 by 1, 0 by -1)
                '-' -> listOf(1 by 0, -1 by 0)
                'L' -> listOf(1 by 0, 0 by -1)
                'J' -> listOf(-1 by 0, 0 by -1)
                '7' -> listOf(-1 by 0, 0 by 1)
                'F' -> listOf(1 by 0, 0 by 1)
                else -> throw Exception()
            }.map { it + p }
            connectingPipes.filter { it != p && it !in loop }
                .forEach { p ->
                    loop[p] = dist
                    currentPoints.add(p)
                }
        }
        loop.toMap()
    }

    override fun problemOne(): Number {
        return ceil(loop.maxBy { it.value }!!.value.toDouble() / 2)
    }

    override fun problemTwo(): Number {
//        val polyPts = loop.keys.toList()
        val polygonPts = loop.keys.toList()
        val polygonVerts = polygonPts.filterNot { input[it.y][it.x] == '|' || input[it.y][it.x] == '-' }

        val innerPts = input.indices
            .flatMap { y -> input[y].indices.map { x -> x by y } }
            .filter { it !in polygonPts }
            .filter { isInPolygon(polygonVerts, it) }

        printInner(innerPts)
        //Not 64
        return innerPts.size
    }

    //  Using https://en.wikipedia.org/wiki/Even%E2%80%93odd_rule
    private fun isInPolygon(poly: List<Point2>, testPoint: Point2): Boolean {
        //      int i, j, c = 0;
//  for (i = 0, j = nvert-1; i < nvert; j = i++) {
//    if ( ((verty[i]>testy) != (verty[j]>testy)) &&
//     (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
//       c = !c;
//  }
//  return c;
        var c = false
        var j = poly.size - 1
        for (i in poly.indices) {
            val vertI = poly[i]
            val vertJ = poly[j]
            if (((vertI.y > testPoint.y) != (vertJ.y > testPoint.y))
                && (testPoint.x < (vertJ.x - vertI.x) * (testPoint.y - vertI.y) / (vertJ.y - vertI.y) + vertI.x))
                        c = !c
            j = i
        }
        return c
//        if (testPoint in poly) return false
//        val x = testPoint.x
//        val y = testPoint.y
//        val num = poly.size
//        var j = num - 1
//        var c = false
//        for (i in 0 until num) {
//            if ((x == poly[i].x) and (y == poly[i].y)) return false
//            if ((poly[i].y > y) != (poly[j].y > y)) {
//                val slope = (x - poly[i].x) * (poly[j].y - poly[i].y)
//                - (poly[j].x - poly[i].x) * (y - poly[i].y)
//                if (slope == 0) return true
//                if ((slope < 0) != (poly[j].x < poly[i].y))
//                    c = !c
//            }
//            j = i
//        }
//        return c
    }

//      int i, j, c = 0;
//  for (i = 0, j = nvert-1; i < nvert; j = i++) {
//    if ( ((verty[i]>testy) != (verty[j]>testy)) &&
//     (testx < (vertx[j]-vertx[i]) * (testy-verty[i]) / (verty[j]-verty[i]) + vertx[i]) )
//       c = !c;
//  }
//  return c;

    private fun printInner(inner: List<Point2>) {
        for (y in input.indices) {
            for (x in input[y].indices) {
                print(if (x by y in inner) 'I' else input[y][x])
            }
            println()
        }
    }
//    num = len(poly)
//    j = num - 1
//    c = False
//    for i in range(num):
//        if (x == poly[i][0]) and (y == poly[i][1]):
//            # point is a corner
//            return True
//        if (poly[i][1] > y) != (poly[j][1] > y):
//            slope = (x - poly[i][0]) * (poly[j][1] - poly[i][1]) - (
//                poly[j][0] - poly[i][0]
//            ) * (y - poly[i][1])
//            if slope == 0:
//                # point is on boundary
//                return True
//            if (slope < 0) != (poly[j][1] < poly[i][1]):
//                c = not c
//        j = i
}