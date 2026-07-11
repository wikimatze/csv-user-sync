package com.example.csvusersync.application.service

import com.example.csvusersync.domain.csv.CsvParser
import com.example.csvusersync.infrastructure.persistence.RoleEntity
import com.example.csvusersync.infrastructure.persistence.RoleRepository
import com.example.csvusersync.infrastructure.persistence.UserEntity
import com.example.csvusersync.infrastructure.persistence.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService (
    private val csvParser: CsvParser,
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {
    @Transactional
    fun saveUsers(lines: List<String>) {
        val users = csvParser.parse(lines)
        val entities = users.map { UserEntity(it.user_id, it.mail) }

        userRepository.saveAll(entities)
    }

    @Transactional
    fun saveRoles(lines: List<String>) {
        val roles = csvParser.parseRoles(lines)

        // Group by userId to handle each user's roles together
        roles.groupBy { it.user_id }.forEach { (user_id, userRoles) ->
            userRepository.findById(user_id)
                .orElseThrow { Exception("User not found: $user_id") }

            // Delete existing roles for this user
            roleRepository.deleteAllByUserId((user_id))
            roleRepository.flush() // arrgh, forgot about this

            userRoles.forEach { role ->
                roleRepository.save(RoleEntity(userId = role.user_id, role = role.role))
            }
        }
    }
}