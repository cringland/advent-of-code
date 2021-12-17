package day15

import java.util.Collections
import java.util.concurrent.TimeUnit


fun main(args: Array<String>) {
    val total: Long = 235
    val startTime = System.currentTimeMillis()

    var i = 1
    while (i <= total) {
        try {
            Thread.sleep(50)
            printProgress(startTime, total, i.toLong())
        } catch (e: InterruptedException) {
        }

        i = i + 3
    }
}


private fun printProgress(startTime: Long, total: Long, current: Long) {
    val eta = if (current == 0L)
        0
    else
        (total - current) * (System.currentTimeMillis() - startTime) / current

    val etaHms = if (current == 0L)
        "N/A"
    else
        String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(eta),
                TimeUnit.MILLISECONDS.toMinutes(eta) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(eta) % TimeUnit.MINUTES.toSeconds(1))

    val string = StringBuilder(140)
    val percent = (current * 100 / total).toInt()
    string.append('\r')
            .append(Collections.nCopies(if (percent == 0) 2 else 2 - Math.log10(percent.toDouble()).toInt(), " ").joinToString(""))
            .append(String.format(" %d%% [", percent))
            .append(Collections.nCopies(percent, "=").joinToString(""))
            .append('>')
            .append(Collections.nCopies(100 - percent, " ").joinToString(""))
            .append(']')
            .append(Collections.nCopies(Math.log10(total.toDouble()).toInt() - Math.log10(current.toDouble()).toInt(), " ").joinToString(""))
            .append(String.format(" %d/%d, ETA: %s", current, total, etaHms))
    print(string)
}
