package com.lnyynet.filecollect.notification.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "message_templates")
class MessageTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(nullable = false)
    var name: String,
    
    @Column(nullable = false)
    var title: String,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,
    
    @Column(nullable = false)
    var type: MessageType,
    
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) 