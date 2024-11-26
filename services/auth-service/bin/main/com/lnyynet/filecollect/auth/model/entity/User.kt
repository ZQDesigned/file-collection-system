package com.lnyynet.filecollect.auth.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(unique = true, nullable = false)
    var username: String,
    
    @Column(nullable = false)
    var password: String,
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,
    
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(null, "", "", UserRole.USER)
}

enum class UserRole {
    USER, ADMIN
} 