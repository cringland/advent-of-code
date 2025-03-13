package day9

import Day
import util.Point2
import util.by
import kotlin.math.max
import kotlin.math.min

class Day9 : Day {

    data class File(val id: Long, val size: Long, val isEmpty: Boolean) {
        override fun toString(): String {
            return (0..this.size - 1).map { if (this.isEmpty) "." else this.id.toString() }.joinToString("")
        }
    }

    private val input =
        inputFile().readText().mapIndexed { i, size -> File(i.toLong() / 2, size.toString().toLong(), i % 2 != 0) }
    fun List<File>.print() = println(this.joinToString(""))
    fun List<File>.checksum():Long {
        var idx = 0L
        return this.fold(0L) { ttl, file ->
            val newTotal = if (!file.isEmpty) {
                ttl + (idx..idx+file.size-1).map { it * file.id }.sum()
            } else ttl
            idx += file.size
            newTotal
        }
    }


    override fun problemOne(): Number {
        val newInput = input.toMutableList()
        do {
            val orgInx = newInput.indexOfLast {!it.isEmpty}
            val fileToBeMoved = newInput[orgInx]
            val remainingSize = fileToBeMoved.size
            val newIndex = newInput.indexOfFirst { it.isEmpty }
            val emptyValue = newInput[newIndex]
            val newFile = File(fileToBeMoved.id, min(emptyValue.size, remainingSize), false)
            newInput[newIndex] = newFile
            if (remainingSize < emptyValue.size) {
                newInput.removeAt(orgInx)
                newInput.add(newIndex + 1, File(0, emptyValue.size - remainingSize, true))
            } else if (remainingSize > emptyValue.size) {
                newInput[orgInx] = File(fileToBeMoved.id, remainingSize - emptyValue.size, false)
            } else newInput.removeAt(orgInx)

        } while(newInput.indexOfFirst { it.isEmpty } < newInput.indexOfLast { !it.isEmpty })
        return newInput.checksum()
    }

    override fun problemTwo(): Number {
        val newInput = input.toMutableList()
        var toCheck = input.filterNot { it.isEmpty }.drop(1).reversed()
        do {
            val fileToBeMoved = toCheck.first()
            toCheck = toCheck.drop(1)
            val orgInx = newInput.indexOf(fileToBeMoved)
            val newIndex = newInput.indexOfFirst { it.isEmpty && it.size >= fileToBeMoved.size }
            if (newIndex != -1 && orgInx > newIndex) {
                val emptyValue = newInput[newIndex]
                newInput[orgInx] = File(0, fileToBeMoved.size, true)
                newInput[newIndex] = fileToBeMoved
                if (emptyValue.size != fileToBeMoved.size) {
                    val newEmpty = File(-1, emptyValue.size - fileToBeMoved.size, true)
                    newInput.add(newIndex + 1, newEmpty)
                }
            }
        } while(toCheck.isNotEmpty())
        return newInput.checksum()
    }
}
