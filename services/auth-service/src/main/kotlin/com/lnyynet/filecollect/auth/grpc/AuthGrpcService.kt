package com.lnyynet.filecollect.auth.grpc

import com.lnyynet.filecollect.auth.security.JwtUtils
import com.lnyynet.filecollect.auth.service.UserService
import io.grpc.stub.StreamObserver
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.security.core.userdetails.UsernameNotFoundException

@GrpcService
class AuthGrpcService(
    private val jwtUtils: JwtUtils,
    private val userService: UserService
) : AuthServiceGrpcKt.AuthServiceCoroutineImplBase() {
    
    override suspend fun validateToken(request: ValidateTokenRequest): ValidateTokenResponse {
        return try {
            val username = jwtUtils.extractUsername(request.token)
            val userDetails = userService.loadUserByUsername(username)
            
            if (jwtUtils.validateToken(request.token, userDetails)) {
                ValidateTokenResponse.newBuilder()
                    .setValid(true)
                    .setUsername(username)
                    .addAllRoles(userDetails.authorities.map { it.authority })
                    .build()
            } else {
                ValidateTokenResponse.newBuilder()
                    .setValid(false)
                    .build()
            }
        } catch (e: Exception) {
            ValidateTokenResponse.newBuilder()
                .setValid(false)
                .build()
        }
    }
    
    override suspend fun getUserInfo(request: GetUserInfoRequest): UserInfo {
        val user = userService.findByUsername(request.username)
            ?: throw UsernameNotFoundException("User not found")
            
        return UserInfo.newBuilder()
            .setId(user.id!!)
            .setUsername(user.username)
            .setRole(user.role.name)
            .build()
    }
} 