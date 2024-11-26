package com.lnyynet.filecollect.auth.service.impl

import com.lnyynet.filecollect.auth.model.dto.*
import com.lnyynet.filecollect.auth.model.entity.User
import com.lnyynet.filecollect.auth.repository.UserRepository
import com.lnyynet.filecollect.auth.service.UserService
import com.lnyynet.filecollect.common.exception.ApiException
import com.lnyynet.filecollect.common.exception.ErrorCode
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserService {

    @Transactional
    override fun createUser(request: CreateUserRequest): UserResponse {
        if (userRepository.findByUsername(request.username) != null) {
            throw ApiException(ErrorCode.VALIDATION_ERROR, "用户名已存在")
        }
        
        val user = userRepository.save(User(
            username = request.username,
            password = passwordEncoder.encode(request.password),
            role = request.role
        ))
        
        return user.toResponse()
    }

    override fun getUserByUsername(username: String): UserResponse {
        return findByUsername(username)?.toResponse() 
            ?: throw ApiException(ErrorCode.NOT_FOUND, "用户不存在")
    }

    override fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    override fun listUsers(page: Int, size: Int): List<UserResponse> {
        return userRepository.findAll(PageRequest.of(page, size))
            .map { it.toResponse() }
            .toList()
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val user = findByUsername(username)
            ?: throw UsernameNotFoundException("用户不存在")
            
        return org.springframework.security.core.userdetails.User
            .withUsername(user.username)
            .password(user.password)
            .roles(user.role.name)
            .build()
    }

    @Transactional
    override fun changePassword(username: String, request: ChangePasswordRequest) {
        val user = findByUsername(username)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "用户不存在")
            
        if (!passwordEncoder.matches(request.oldPassword, user.password)) {
            throw ApiException(ErrorCode.VALIDATION_ERROR, "旧密码不正确")
        }
        
        user.password = passwordEncoder.encode(request.newPassword)
        userRepository.save(user)
    }

    @Transactional
    override fun resetPassword(request: ResetPasswordRequest) {
        val user = findByUsername(request.username)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "用户不存在")
            
        user.password = passwordEncoder.encode(request.newPassword)
        userRepository.save(user)
    }

    private fun User.toResponse() = UserResponse(
        id = id!!,
        username = username,
        role = role
    )
} 