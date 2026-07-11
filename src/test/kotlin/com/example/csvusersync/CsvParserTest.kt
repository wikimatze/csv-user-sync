package com.example.csvusersync

import org.junit.jupiter.api.Test
import kotlin.io.path.createTempFile
import kotlin.io.path.writeText
import kotlin.test.assertEquals

class CsvParserTest {

    private val parser = CsvParser();

    @Test
    fun `parse return users from CSV file`() {
        val tempCsvFile = createTempFile(prefix = "user", suffix = ".csv")
        tempCsvFile.writeText("user_id,mail\n1,foo@bar.de\n2,test@test.de")

        val users = parser.parse(tempCsvFile)
        assertEquals(2, users.size)
    }
}