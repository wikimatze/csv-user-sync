package com.example.csvusersync

class CsvParseException(
    message: String,
    val line: Int? = null
) : Exception(if (line != null) "CSV parse error at line $line: $message" else "CSV parse error: $message")