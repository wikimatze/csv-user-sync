package com.example.csvusersync.domain.csv

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.io.path.createTempFile
import kotlin.io.path.readLines
import kotlin.io.path.writeText
import kotlin.test.assertEquals

class CsvParserTest {

    private val parser = CsvParser()

    @Test
    fun `parse() return users from CSV file`() {
        val tempCsvFile = createTempFile(prefix = "users", suffix = ".csv")
        tempCsvFile.writeText("user_id,mail\n1,foo@bar.de")

        val users = parser.parse(tempCsvFile.readLines())
        assertEquals("1", users[0].user_id)
        assertEquals("foo@bar.de", users[0].mail)
    }


    @Test
    fun `parse return filtered users from CSV file`() {
        val tempCsvFile = createTempFile(prefix = "user", suffix = ".csv")
        tempCsvFile.writeText("user_id,mail\n1,foo@bar.de\n2,matze@wikimatze.de")

        val users = parser.parse(tempCsvFile.readLines(), { user -> user.mail.endsWith("bar.de")})
        assertEquals("2", users[0].user_id)
        assertEquals("matze@wikimatze.de", users[0].mail)
    }

    @Test
    fun `parse() should throw exception for empty file parsing CSV`() {
        val tempCsvFile = createTempFile(prefix = "users", suffix = ".csv")
        tempCsvFile.writeText("")

        val exception = assertThrows<CsvParseException> { parser.parse(tempCsvFile.readLines()) }
        assertEquals("CSV parse error: file is empty", exception.message)
    }

    @Test
    fun `parse() should throw exception when parsing CSV file with wrong headline data`() {
        val tempCsvFile = createTempFile(prefix = "users", suffix = ".csv")
        tempCsvFile.writeText("wrong,mail\n1,foo@bar.de")

        val exception = assertThrows<CsvParseException> { parser.parse(tempCsvFile.readLines()) }
        assertEquals("CSV parse error: expected header 'user_id,mail' but was 'wrong,mail'", exception.message)
    }

    @Test
    fun `parse() should throw exception when parsing CSV file wrong data`() {
        val tempCsvFile = createTempFile(prefix = "users", suffix = ".csv")
        tempCsvFile.writeText("user_id,mail\n1,foo@bar.de\n1,2,foo@bar.de")

        val exception = assertThrows<CsvParseException> { parser.parse(tempCsvFile.readLines()) }
        assertEquals("CSV parse error at line 3: expected 2 fields, but got 3", exception.message)
    }

    @Test
    fun `parse() should throw exception when parsing CSV file empty user_id data`() {
        val tempCsvFile = createTempFile(prefix = "users", suffix = ".csv")
        tempCsvFile.writeText("user_id,mail\n,foo@bar.de")

        val exception = assertThrows<CsvParseException> { parser.parse(tempCsvFile.readLines()) }
        assertEquals("CSV parse error at line 2: user_id is blank", exception.message)
    }

    @Test
    fun `parse() should throw exception when parsing CSV file empty mail data`() {
        val tempCsvFile = createTempFile(prefix = "users", suffix = ".csv")
        tempCsvFile.writeText("user_id,mail\n1,")

        val exception = assertThrows<CsvParseException> { parser.parse(tempCsvFile.readLines()) }
        assertEquals("CSV parse error at line 2: mail is blank", exception.message)
    }

    @Test
    fun `parseRoles() return roles from CSV file`() {
        val tempCsvFile = createTempFile(prefix = "roles", suffix = ".csv")
        tempCsvFile.writeText("user_id,role\n1,read")

        val roles = parser.parseRoles(tempCsvFile.readLines())
        assertEquals("1", roles[0].user_id)
        assertEquals("read", roles[0].role)
    }

    @Test
    fun `parseRoles() should throw exception for empty file parsing CSV`() {
        val tempCsvFile = createTempFile(prefix = "roles", suffix = ".csv")
        tempCsvFile.writeText("")

        val exception = assertThrows<CsvParseException> { parser.parseRoles(tempCsvFile.readLines()) }
        assertEquals("CSV parse error: file is empty", exception.message)
    }

    @Test
    fun `parseRoles() should throw exception when parsing CSV file with wrong headline data`() {
        val tempCsvFile = createTempFile(prefix = "roles", suffix = ".csv")
        tempCsvFile.writeText("wrong,role\n1,read")

        val exception = assertThrows<CsvParseException> { parser.parseRoles(tempCsvFile.readLines()) }
        assertEquals("CSV parse error: expected header 'user_id,role' but was 'wrong,role'", exception.message)
    }

    @Test
    fun `parseRoles() should throw exception when parsing CSV file wrong data`() {
        val tempCsvFile = createTempFile(prefix = "roles", suffix = ".csv")
        tempCsvFile.writeText("user_id,role\n1,read\n1,2,read")

        val exception = assertThrows<CsvParseException> { parser.parseRoles(tempCsvFile.readLines()) }
        assertEquals("CSV parse error at line 3: expected 2 fields, but got 3", exception.message)
    }

    @Test
    fun `parseRoles() should throw exception when parsing CSV file empty user_id data`() {
        val tempCsvFile = createTempFile(prefix = "roles", suffix = ".csv")
        tempCsvFile.writeText("user_id,role\n,read")

        val exception = assertThrows<CsvParseException> { parser.parseRoles(tempCsvFile.readLines()) }
        assertEquals("CSV parse error at line 2: user_id is blank", exception.message)
    }

    @Test
    fun `parseRoles() should throw exception when parsing CSV file empty role data`() {
        val tempCsvFile = createTempFile(prefix = "roles", suffix = ".csv")
        tempCsvFile.writeText("user_id,role\n1,")

        val exception = assertThrows<CsvParseException> { parser.parseRoles(tempCsvFile.readLines()) }
        assertEquals("CSV parse error at line 2: role is blank", exception.message)
    }
}