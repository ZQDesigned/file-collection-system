package com.lnyynet.filecollect.gateway.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import javax.crypto.SecretKey

@Service
class JwtService {
    @Value("\${jwt.secret}")
    private lateinit var secret: String
    
    private val key: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8))
    }
    
    private val parser by lazy {
        Jwts.parser()
            .verifyWith(key)
            .build()
    }
    
    fun validateToken(token: String): Claims {
        return parser.parseSignedClaims(token).payload
    }
} 