package com.example.csvusersync

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CsvUserSyncApplication

fun main(args: Array<String>) {
    runApplication<CsvUserSyncApplication>(*args)
}
