package com.lnyynet.filecollect.auth.model.dto

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "用户名不能为空")
    val username: String,
    
    @field:NotBlank(message = "密码不能为空")
    val password: String
)

data class TokenResponse(
    val token: String,
    val refreshToken: String,
    val expiresIn: Long
)

data class RefreshTokenRequest(
    @field:NotBlank(message = "刷新令牌不能为空")
    val refreshToken: String
) 