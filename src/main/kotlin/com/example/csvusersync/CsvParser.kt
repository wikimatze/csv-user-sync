package com.example.csvusersync

class CsvParser {
    fun parse(lines: List<String>): List<User> {

        val header = lines.first()
        require(header == "user_id,mail") {
            throw CsvParseException("expected header 'user_id,email' but was '$header'")
        }

        return lines.drop(1).mapIndexed { index, line ->
            val parts = line.split(",")

            println(parts)
            println(parts.size)

            require(parts.size == 2) {
                throw CsvParseException("expected 2 fields, but got ${parts.size}", line = index  + 2)
            }

            val userId = parts[0]
            require(userId.isNotBlank()) {
                throw CsvParseException("user_id is blank", line = index  + 2)
            }


            val mail = parts[1]
            require(mail.isNotBlank()) {
                throw CsvParseException("mail is blank", line = index  + 2)
            }

            User(parts[0], parts[1])
        }
    }
}