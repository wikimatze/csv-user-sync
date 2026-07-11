package com.example.csvusersync

class CsvParser {
    fun parse(lines: List<String>): List<User> {

        val header = lines.first()
        require(header == "user_id,mail") {
            throw CsvParseException("expected header 'user_id,email' but was '$header'")
        }
        
        return lines.drop(1).map { line ->
            val parts = line.split(",")
            User(parts[0], parts[1])
        }
    }
}