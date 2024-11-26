package com.lnyynet.filecollect.notification.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.beans.factory.annotation.Value
import java.util.Properties

@Configuration
class EmailConfig {
    @Value("\${spring.mail.host}")
    private lateinit var host: String
    
    @Value("\${spring.mail.port}")
    private var port: Int = 0
    
    @Value("\${spring.mail.username}")
    private lateinit var username: String
    
    @Value("\${spring.mail.password}")
    private lateinit var password: String
    
    @Bean
    fun javaMailSender(): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.host = host
        mailSender.port = port
        mailSender.username = username
        mailSender.password = password
        
        val props = Properties()
        props["mail.transport.protocol"] = "smtp"
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.debug"] = "true"
        
        mailSender.javaMailProperties = props
        return mailSender
    }
} 