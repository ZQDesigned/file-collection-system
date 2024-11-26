package com.lnyynet.filecollect.notification.model.dto

import com.lnyynet.filecollect.notification.model.entity.MessageStatus
import com.lnyynet.filecollect.notification.model.entity.MessageType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class CreateMessageRequest(
    @field:NotBlank
    val userId: String,
    
    @field:NotBlank
    val title: String,
    
    @field:NotBlank
    val content: String,
    
    @field:NotNull
    val type: MessageType
)

data class MessageResponse(
    val id: Long,
    val userId: String,
    val title: String,
    val content: String,
    val type: MessageType,
    val status: MessageStatus,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class UpdateMessageStatusRequest(
    @field:NotNull
    val status: MessageStatus
)

data class MessageStats(
    val totalCount: Long,
    val unreadCount: Long
) 