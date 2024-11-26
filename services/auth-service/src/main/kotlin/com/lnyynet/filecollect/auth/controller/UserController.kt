package com.lnyynet.filecollect.auth.controller

import com.lnyynet.filecollect.auth.model.dto.*
import com.lnyynet.filecollect.auth.service.UserService
import com.lnyynet.filecollect.common.model.Response
import jakarta.validation.Valid
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun createUser(@Valid @RequestBody request: CreateUserRequest): Response<UserResponse> {
        return Response.success(userService.createUser(request))
    }
    
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    fun getUser(@PathVariable username: String): Response<UserResponse> {
        return Response.success(userService.getUserByUsername(username))
    }

    @PostMapping("/change-password")
    fun changePassword(
        @Valid @RequestBody request: ChangePasswordRequest,
        authentication: Authentication
    ): Response<Nothing> {
        userService.changePassword(authentication.name, request)
        return Response.success()
    }

    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    fun resetPassword(@Valid @RequestBody request: ResetPasswordRequest): Response<Nothing> {
        userService.resetPassword(request)
        return Response.success()
    }
} 