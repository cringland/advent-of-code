package day13

import Day


class Day13 : Day {
    class Node(val value: Pair<List<Node>?, Int?>) : Comparable<Node> {
        constructor(int: Int) : this(null to int)
        constructor(listNodes: List<Node>) : this(listNodes to null)

        val isList = this.value.first != null
        val isInt = this.value.second != null
        val list get() = this.value.first!!
        val int get() = this.value.second!!

        override fun compareTo(other: Node): Int {
            if (this.isInt && other.isInt) return this.int.compareTo(other.int)

            val leftList = if (this.isList) list else listOf(Node(this.int))
            val rightList = if (other.isList) other.list else listOf(Node(other.int))

            if (leftList.isEmpty() && rightList.isEmpty()) return 0
            for (i in leftList.indices) {
                if (i == rightList.size) return 1
                val comparison = leftList[i].compareTo(rightList[i])
                if (comparison != 0) return comparison
            }
            return -1
        }

        override fun toString(): String {
            return if (this.isInt)
                this.int.toString()
            else
                this.list.toString()
        }
    }

    fun node(str: String): Pair<String, Node> {
        var s = str.drop(1) // Remove first [
        val nodes: MutableList<Node> = mutableListOf()
        while (s[0] != ']') { // until last brace
            when {
                s[0].isDigit() -> {
                    val sint = s.takeWhile { it != ',' && it != ']' }
                    s = s.drop(sint.length)
                    nodes.add(Node(sint.toInt()))
                }
                s[0] == ',' -> s = s.drop(1)
                s[0] == '[' -> {
                    val p = node(s)
                    s = p.first
                    nodes.add(p.second)
                }
            }
        }
        return s.drop(1) to Node(nodes.toList())
    }

    private val input = inputFile().readLines().filter(String::isNotEmpty).map { node(it).second }

    override fun problemOne(): Number {
        return input.chunked(2).mapIndexedNotNull { i, it ->
            if (it[0] < it[1]) i + 1 else null
        }.sum()
    }

    override fun problemTwo(): Number {
        val divs = listOf(node("[[2]]").second, node("[[6]]").second)
        return (input + divs).sorted().mapIndexedNotNull { i, it ->
            if (it in divs) i + 1 else null
        }.fold(1) { a, b -> a * b }
    }

    private fun List<Node>.print() = print(this.joinToString(separator = "\n\n", transform = Node::toString))
}
