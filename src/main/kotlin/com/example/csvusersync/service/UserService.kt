package com.example.csvusersync.service

import com.example.csvusersync.domain.csv.CsvParser
import com.example.csvusersync.entity.UserEntity
import com.example.csvusersync.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService (
    private val csvParser: CsvParser,
    private val userRepository: UserRepository
) {
    @Transactional
    fun parseAndSave(lines: List<String>): List<UserEntity> {
        val users = csvParser.parse(lines)
        val entities = users.map { UserEntity(it.user_id, it.mail) }

        return userRepository.saveAll(entities)
    }
}