package com.example.csvusersync

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

import kotlin.io.path.createTempFile
import kotlin.io.path.readLines
import kotlin.io.path.writeText
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CsvParserTest {

    private val parser = CsvParser();

    @Test
    fun `parse return users from CSV file`() {
        val tempCsvFile = createTempFile(prefix = "user", suffix = ".csv")
        tempCsvFile.writeText("user_id,mail\n1,foo@bar.de")

        val users = parser.parse(tempCsvFile.readLines())
        assertEquals("1", users[0].user_id)
        assertEquals("foo@bar.de", users[0].email)
    }

    @Test
    fun `should throw exception when parsing CSV file with incorrect data`() {
        val tempCsvFile = createTempFile(prefix = "user", suffix = ".csv")
        tempCsvFile.writeText("wrong,mail\n1,foo@bar.de")

        val exception = assertThrows<CsvParseException> { parser.parse(tempCsvFile.readLines()) }
        assertEquals("CSV parse error: expected header 'user_id,email' but was 'wrong,mail'", exception.message)
    }
}