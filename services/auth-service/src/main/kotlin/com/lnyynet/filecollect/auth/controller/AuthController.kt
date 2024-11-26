package com.lnyynet.filecollect.auth.controller

import com.lnyynet.filecollect.auth.model.dto.LoginRequest
import com.lnyynet.filecollect.auth.model.dto.TokenResponse
import com.lnyynet.filecollect.auth.model.dto.RefreshTokenRequest
import com.lnyynet.filecollect.auth.service.AuthService
import com.lnyynet.filecollect.common.model.Response
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): Response<TokenResponse> {
        return Response.success(authService.login(request))
    }
    
    @PostMapping("/refresh")
    fun refresh(@Valid @RequestBody request: RefreshTokenRequest): Response<TokenResponse> {
        return Response.success(authService.refreshToken(request))
    }
} 