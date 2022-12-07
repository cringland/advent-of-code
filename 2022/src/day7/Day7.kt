package day7

import Day

class Day7 : Day {
    class Dir(val path: String, val name: String, val parent: Dir?, var files: Map<String, Long> = emptyMap(), var dirs: Map<String, Dir> = emptyMap()) {
        fun size(): Long = files.values.sum() + dirs.values.map { it.size() }.sum()
        fun findDirByPath(str: String): Dir? {
            return if (str == this.path) this
            else dirs.values.find { it.findDirByPath(str) != null }?.findDirByPath(str)
        }

        fun findAll(predicate: (Dir) -> Boolean): List<Dir> {
            val list = mutableListOf<Dir>()
            if (predicate(this)) list.add(this)
            return (list + dirs.values.flatMap { it.findAll(predicate) })
        }

        fun addFile(file: Pair<String, Long>) {
            files = (files + file)
        }

        fun addDir(dir: Dir) {
            dirs = (dirs + (dir.path to dir))
        }
    }

    val root = fileSystem()

    fun fileSystem(): Dir {
        val root = Dir("/", "/", null)
        var cwd = "/"
        var currentDir = root
        inputFile().readText().split("$ ").filter(String::isNotEmpty).drop(1) // drop cd /
                .forEach { cmd ->
                    if (cmd.startsWith("cd")) {
                        val dir = cmd.trim().split(" ")[1]
                        when (dir) {
                            ".." -> {
                                cwd = cwd.dropLast(1).dropLastWhile { d -> d != '/' }
                                currentDir = currentDir.parent!!
                            }
                            else -> {
                                cwd = "$cwd$dir/"
                                currentDir = root.findDirByPath(cwd)!!
                            }
                        }
                    } else { // ls
                        val allFiles = cmd.trim().split("\n").drop(1)
                                .groupBy { file ->
                                    if (file.startsWith("dir"))
                                        "DIR"
                                    else "FILE"
                                }
                        allFiles["DIR"]?.map { dir -> dir.split(" ")[1] }
                                ?.forEach { dir ->
                                    currentDir.addDir(Dir("$cwd$dir/", dir, currentDir))
                                }
                        allFiles["FILE"]?.map { file ->
                            val sp = file.split(" ")
                            sp[1] to sp[0].toLong()
                        }?.forEach { file -> currentDir.addFile(file) }
                    }
                }
        return root
    }

    override fun problemOne(): Long {
        return root.findAll { it.size() <= 100000 }.map { it.size() }.sum()
    }

    override fun problemTwo(): Long {
        val total = 70000000
        val req = 30000000
        val totalUsed = root.size()
        val needed = totalUsed - (total - req)
        return root.findAll { true }.map { it.size() }.filter { it >= needed }.min()!!
    }
}
