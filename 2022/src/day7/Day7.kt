package day7

import Day

class Day7 : Day {
    class Dir(val path: String, val parent: Dir?, var files: Map<String, Long> = emptyMap(), var dirs: Map<String, Dir> = emptyMap()) {
        fun size(): Long = files.values.sum() + dirs.values.map { it.size() }.sum()
        fun findDirByPath(str: String): Dir? {
            if (str == this.path) return this
            for (dir in dirs.values) {
                val t = dir.findDirByPath(str)
                if (t != null) return t
            }
            return null
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

    private val root: Dir = Dir("/", null).let {
        var currentDir = it
        inputFile().readText().split("$ ").filter(String::isNotEmpty).drop(1) // drop cd /
                .forEach { cmd ->
                    if (cmd.startsWith("cd")) {
                        val dir = cmd.trim().split(" ")[1]
                        currentDir = when (dir) {
                            ".." -> currentDir.parent!!
                            else -> currentDir.findDirByPath("${currentDir.path}$dir/")!!
                        }
                    } else {
                        cmd.trim().split("\n").drop(1)
                                .forEach { file ->
                                    if (file.startsWith("dir")) {
                                        val dir = file.split(" ")[1]
                                        currentDir.addDir(Dir("${currentDir.path}$dir/", currentDir))
                                    } else {
                                        val temp = file.split(" ")
                                        currentDir.addFile(temp[1] to temp[0].toLong())
                                    }
                                }
                    }
                }
        it
    }

    override fun problemOne(): Long {
        return root.findAll { it.size() <= 100000 }.map { it.size() }.sum()
    }

    override fun problemTwo(): Long {
        val req = 40000000
        val totalUsed = root.size()
        val needed = totalUsed - req
        return root.findAll { true }.map { it.size() }.filter { it >= needed }.min()!!
    }
}
