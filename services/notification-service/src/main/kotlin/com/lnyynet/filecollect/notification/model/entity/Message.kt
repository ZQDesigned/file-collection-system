package com.lnyynet.filecollect.notification.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "messages")
class Message(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(nullable = false)
    var userId: String,
    
    @Column(nullable = false)
    var title: String,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: MessageType,
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: MessageStatus = MessageStatus.UNREAD,
    
    @ElementCollection
    @CollectionTable(
        name = "message_tags",
        joinColumns = [JoinColumn(name = "message_id")]
    )
    @Column(name = "tag")
    var tags: MutableSet<String> = mutableSetOf(),
    
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

enum class MessageType {
    SYSTEM,
    TASK,
    SUBMISSION
}

enum class MessageStatus {
    UNREAD,
    READ,
    ARCHIVED
} 