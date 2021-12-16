package day16

import Day
import java.io.File


class Day16 : Day {

    private val inputPacket = File("src/day16/input").readText().replace("\n", "")
            .hexToBinaryString().extractPacket().second

    override fun problemOne(): Int {
        return inputPacket.countVersions()
    }

    override fun problemTwo(): Long {
        return inputPacket.calculate()
    }

    data class Packet(val version: Int, val id: Int, val literalValue: Long?, val subPackets: List<Packet>? = emptyList()) {
        fun countVersions(): Int {
            return this.version + (this.subPackets?.map { it.countVersions() }?.sum() ?: 0)
        }

        fun calculate(): Long {
            if (this.id == 4) {
                return this.literalValue!!
            }
            val calcedSubs = this.subPackets!!.map { it.calculate() }
            return when (this.id) {
                0 -> calcedSubs.sum()
                1 -> calcedSubs.fold(1L) { acc, l -> acc * l }
                2 -> calcedSubs.min()!!
                3 -> calcedSubs.max()!!
                5 -> if (calcedSubs[0] > calcedSubs[1]) 1 else 0
                6 -> if (calcedSubs[0] < calcedSubs[1]) 1 else 0
                7 -> if (calcedSubs[0] == calcedSubs[1]) 1 else 0
                else -> throw RuntimeException("?")
            }
        }
    }

    private fun String.extractPacket(): Pair<String, Packet> {
        var str = this
        val version = str.substring(0, 3).toInt(2)
        val id = str.substring(3, 6).toInt(2)
        str = str.substring(6)
        val literal = if (id == 4) {
            var literalStr = ""
            do {
                val current = str.substring(0, 5)
                literalStr += current.substring(1)
                str = str.substring(5)
            } while (current.startsWith("1"))
            literalStr.toLong(2)
        } else null

        val subPackets = if (id != 4) {
            val lengthType = str.substring(0, 1).toInt()
            str = str.substring(1)
            if (lengthType == 0) {
                //Length in bits
                val length = str.substring(0, 15).toInt(2)
                str = str.substring(15)
                var subPacketStr = str.substring(0, length)
                str = str.substring(length)
                val subPacks = mutableListOf<Packet>()
                while (subPacketStr != "") {
                    val result = subPacketStr.extractPacket()
                    subPacketStr = result.first
                    subPacks.add(result.second)
                }
                subPacks.toList()
            } else {
                //Length in sub packets
                val length = str.substring(0, 11).toInt(2)
                str = str.substring(11)
                (1..length).map {
                    val result = str.extractPacket()
                    str = result.first
                    result.second
                }
            }
        } else null
        val packet = Packet(version, id, literal, subPackets)
        return str to packet
    }

    private fun String.hexToBinaryString(): String {
        return this.map {
            it.toString().toInt(16).toString(2).padStart(4, '0')
        }.joinToString("")
    }
}

