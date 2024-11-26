package com.lnyynet.filecollect.notification.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "email_templates")
class EmailTemplate(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(nullable = false)
    var name: String,
    
    @Column(nullable = false)
    var subject: String,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,
    
    @Column(nullable = false)
    var isHtml: Boolean = false,
    
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) 