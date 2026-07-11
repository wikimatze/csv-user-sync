package com.example.csvusersync

class CsvParser {
    fun parse(lines: List<String>): List<User> =
        lines.drop(1).map { line ->
            val parts = line.split(",")
            User(parts[0], parts[1])
        }
}