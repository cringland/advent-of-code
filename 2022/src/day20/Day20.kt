package day20

import Day

//Stolen from deprecated mod function
fun Long.mod2(other: Long): Long {
    val r = this % other
    return r + (other and (((r xor other) and (r or -r)) shr 31))
}

class Day20 : Day {

    data class Node(val startIndex: Int, val value: Long)

    data class Message(val list: MutableList<Node>) {
        fun answer(): Long {
            val idxOf0 = list.indexOfFirst { it.value == 0L }
            return listOf(1000, 2000, 3000).fold(0L) { acc, it -> acc + list[(idxOf0 + it) % list.size].value }
        }

        fun solve(times: Int) = list.toList().let { order -> repeat(times) { order.toList().forEach { move(it) } } }

        fun move(node: Node) {
            val currentIndex = list.indexOfFirst { it.startIndex == node.startIndex }
            val removed = list.removeAt(currentIndex)
            list.add((currentIndex.toLong() + removed.value).mod2(list.size.toLong()).toInt(), node)
        }
    }


    private val input = inputFile().readLines().mapIndexed { i, it -> Node(i, it.toLong()) }

    override fun problemOne(): Number {
        val msg = Message(input.toMutableList())
        msg.solve(1)
        return msg.answer()
    }

    override fun problemTwo(): Number {
        val key = 811589153L
        val msg = Message(input.map { Node(it.startIndex, it.value * key) }.toMutableList())
        msg.solve(10)
        return msg.answer()
    }
}
