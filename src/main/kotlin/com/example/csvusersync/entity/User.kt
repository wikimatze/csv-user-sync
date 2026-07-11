package com.example.csvusersync.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @Column(name = "user_id")
    val userId: String,

    @Column(name = "mail")
    val email: String
) {
    // JPA requires no-arg constructor
    protected constructor() : this("", "") {
    }
}
