package util

fun List<Long>.lcm() = this.sorted().reduce { v1, v2 -> v1.lcm(v2) }
fun Long.lcm(that: Long): Long {
    val largest = this.coerceAtLeast(that)
    var lcm = largest
    while (true) {
        if (((lcm % this) == 0L) && ((lcm % that) == 0L))
            return lcm
        lcm += largest
    }
}