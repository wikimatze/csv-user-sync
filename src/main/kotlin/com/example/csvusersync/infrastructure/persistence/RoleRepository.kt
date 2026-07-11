package com.example.csvusersync.infrastructure.persistence

import jakarta.persistence.Embeddable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<RoleEntity, RoleEntityId> {
    fun findAllByUserId(userId: String): MutableList<RoleEntity>
    fun deleteAllByUserId(userId: String)
}

@Embeddable
public class RoleEntityId(
    val userId: String,
    val role: String
) : java.io.Serializable {
    constructor() : this("","") {
    }
}