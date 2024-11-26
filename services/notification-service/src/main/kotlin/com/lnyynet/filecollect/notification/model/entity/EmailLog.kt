package com.lnyynet.filecollect.notification.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "email_logs")
class EmailLog(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(nullable = false)
    var recipient: String,
    
    @Column(nullable = false)
    var subject: String,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,
    
    @Column(nullable = false)
    var status: EmailStatus = EmailStatus.PENDING,
    
    @Column
    var error: String? = null,
    
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class EmailStatus {
    PENDING,
    SENT,
    FAILED
} 