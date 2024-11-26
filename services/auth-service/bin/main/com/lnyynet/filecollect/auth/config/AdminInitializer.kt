package com.lnyynet.filecollect.auth.config

import com.lnyynet.filecollect.auth.model.entity.User
import com.lnyynet.filecollect.auth.model.entity.UserRole
import com.lnyynet.filecollect.auth.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        if (userRepository.findByUsername("admin") == null) {
            val admin = User(
                username = "admin",
                password = passwordEncoder.encode("admin123"),
                role = UserRole.ADMIN
            )
            userRepository.save(admin)
        }
    }
} 