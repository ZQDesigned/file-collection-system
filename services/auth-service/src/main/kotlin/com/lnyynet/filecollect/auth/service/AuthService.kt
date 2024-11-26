package com.lnyynet.filecollect.auth.service

import com.lnyynet.filecollect.auth.model.dto.LoginRequest
import com.lnyynet.filecollect.auth.model.dto.TokenResponse
import com.lnyynet.filecollect.auth.model.dto.RefreshTokenRequest

interface AuthService {
    fun login(request: LoginRequest): TokenResponse
    fun refreshToken(request: RefreshTokenRequest): TokenResponse
} 