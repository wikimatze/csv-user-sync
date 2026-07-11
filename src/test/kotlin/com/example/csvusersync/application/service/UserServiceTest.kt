package com.example.csvusersync.application.service

import com.example.csvusersync.infrastructure.persistence.RoleEntityId
import com.example.csvusersync.infrastructure.persistence.RoleRepository
import com.example.csvusersync.infrastructure.persistence.UserRepository
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
@Transactional
class UserServiceTest(
    @Autowired val userService: UserService,
    @Autowired val userRepository: UserRepository,
    @Autowired private val roleRepository: RoleRepository
) {
    @Test
    fun `saveUsers with valid data from CSV file`() {
        val csvLines = listOf(
            "user_id,mail",
            "1,foo@bar.de",
            "2,bar@foo.de"
        )

        userService.saveUsers(csvLines)


        val savedUsers = userRepository.findAll()

        assertEquals(2, savedUsers.size)
        assertEquals("2", savedUsers[1].userId)
        assertEquals("bar@foo.de", savedUsers[1].mail)
    }

    @Test
    fun `saveRoles roles_from_csv fails because of missing user`() {
        val rolesLines = listOf(
            "user_id,role",
            "1,read",
        )

        val exception = assertThrows<Exception> { userService.saveRoles(rolesLines)}
        assertEquals("User not found: 1", exception.message)
    }

    @Test
    fun `saveRoles roles_from_csv save roles for existing users`() {
        val csvLines = listOf(
            "user_id,mail",
            "1,foo@bar.de",
            "2,bar@foo.de"
        )

        userService.saveUsers(csvLines)

        val rolesLines = listOf(
            "user_id,role",
            "1,read",
            "2,write",
        )

        userService.saveRoles(rolesLines)

        val dbResults = roleRepository.findAll()

        assertEquals(2, dbResults.size)
    }

    @Test
    fun `saveRoles roles_from_csv save will update existing roles`() {
        val csvLines = listOf(
            "user_id,mail",
            "1,foo@bar.de",
        )

        userService.saveUsers(csvLines)

        val rolesLines = listOf(
            "user_id,role",
            "1,read",
            "1,write",
        )

        userService.saveRoles(rolesLines)

        val dbResults = roleRepository.findAllByUserId(("1"))

        assertEquals(2, dbResults.size)

        val updatedRolesLines = listOf(
            "user_id,role",
            "1,read",
        )

        userService.saveRoles(updatedRolesLines)

        val updatedResults = roleRepository.findAllByUserId(("1"))

        assertEquals(1, updatedResults.size)
    }
}