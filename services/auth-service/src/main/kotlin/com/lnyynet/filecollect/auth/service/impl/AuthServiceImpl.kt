package com.lnyynet.filecollect.auth.service.impl

import com.lnyynet.filecollect.auth.model.dto.LoginRequest
import com.lnyynet.filecollect.auth.model.dto.TokenResponse
import com.lnyynet.filecollect.auth.model.dto.RefreshTokenRequest
import com.lnyynet.filecollect.auth.security.JwtUtils
import com.lnyynet.filecollect.auth.service.AuthService
import com.lnyynet.filecollect.common.exception.ApiException
import com.lnyynet.filecollect.common.exception.ErrorCode
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Value

@Service
class AuthServiceImpl(
    private val authenticationManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtUtils: JwtUtils,
    @Value("\${jwt.expiration}") private val expiration: Long
) : AuthService {

    override fun login(request: LoginRequest): TokenResponse {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.username, request.password)
        )
        
        val userDetails = authentication.principal as org.springframework.security.core.userdetails.UserDetails
        val token = jwtUtils.generateToken(userDetails)
        val refreshToken = jwtUtils.generateRefreshToken(userDetails)
        
        return TokenResponse(token, refreshToken, expiration)
    }

    override fun refreshToken(request: RefreshTokenRequest): TokenResponse {
        try {
            val username = jwtUtils.extractUsername(request.refreshToken)
            val userDetails = userDetailsService.loadUserByUsername(username)
            
            if (jwtUtils.validateToken(request.refreshToken, userDetails)) {
                val token = jwtUtils.generateToken(userDetails)
                val refreshToken = jwtUtils.generateRefreshToken(userDetails)
                return TokenResponse(token, refreshToken, expiration)
            }
            
            throw ApiException(ErrorCode.UNAUTHORIZED, "无效的刷新令牌")
        } catch (e: Exception) {
            throw ApiException(ErrorCode.UNAUTHORIZED, "无效的刷新令牌")
        }
    }
} 