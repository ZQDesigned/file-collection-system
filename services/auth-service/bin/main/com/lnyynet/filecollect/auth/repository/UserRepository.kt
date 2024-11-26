package com.lnyynet.filecollect.auth.repository

import com.lnyynet.filecollect.auth.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User?
} 