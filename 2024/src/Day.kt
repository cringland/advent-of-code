import java.io.File

interface Day {
    fun problemOne(): Any
    fun problemTwo(): Any
    fun inputFile(): File = File("src/${this.javaClass.packageName}/input")
}
