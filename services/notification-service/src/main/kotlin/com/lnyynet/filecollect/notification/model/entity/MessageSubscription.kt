package com.lnyynet.filecollect.notification.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "message_subscriptions")
class MessageSubscription(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(nullable = false)
    var userId: String,
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: MessageType,
    
    @Column(nullable = false)
    var enabled: Boolean = true,
    
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) 