package com.lnyynet.filecollect.auth.service

import com.lnyynet.filecollect.auth.model.dto.*
import com.lnyynet.filecollect.auth.model.entity.User
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService : UserDetailsService {
    fun createUser(request: CreateUserRequest): UserResponse
    fun getUserByUsername(username: String): UserResponse
    fun findByUsername(username: String): User?
    fun listUsers(page: Int, size: Int): List<UserResponse>
    fun changePassword(username: String, request: ChangePasswordRequest)
    fun resetPassword(request: ResetPasswordRequest)
} 