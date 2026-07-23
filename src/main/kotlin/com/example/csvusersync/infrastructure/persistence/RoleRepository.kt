package com.example.csvusersync.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<RoleEntity, Long> {
    fun findAllByUserId(userId: String): MutableList<RoleEntity>
    fun deleteAllByUserId(userId: String)
}

