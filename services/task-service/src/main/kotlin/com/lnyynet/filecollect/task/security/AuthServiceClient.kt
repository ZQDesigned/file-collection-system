package com.lnyynet.filecollect.task.security

import com.lnyynet.filecollect.auth.grpc.AuthServiceGrpc
import com.lnyynet.filecollect.auth.grpc.ValidateTokenRequest
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service

@Service
class AuthServiceClient {
    @GrpcClient("auth-service")
    private lateinit var authStub: AuthServiceGrpc.AuthServiceBlockingStub
    
    fun validateToken(token: String): Boolean {
        return try {
            val request = ValidateTokenRequest.newBuilder()
                .setToken(token)
                .build()
            val response = authStub.validateToken(request)
            response.valid
        } catch (e: Exception) {
            false
        }
    }
} 