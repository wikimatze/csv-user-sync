package com.example.csvusersync.service

import com.example.csvusersync.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest
@Transactional
class UserServiceTest(
    @Autowired val userService: UserService,
    @Autowired val userRepository: UserRepository
) {
    @Test
    fun `parseAndSave users_from_csv with valid data`() {
        val csvLines = listOf(
            "user_id,mail",
            "1,foo@bar.de",
            "2,bar@foo.de"
        )

        val savedUsers = userService.parseAndSave(csvLines)

        assertEquals(2, savedUsers.size)
        assertEquals("2", savedUsers[1].userId)
        assertEquals("bar@foo.de", savedUsers[1].mail)

        val dbResults = userRepository.findAll()
        assertEquals(2, dbResults.size)
    }
}