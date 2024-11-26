package com.lnyynet.filecollect.notification.model.dto

import com.lnyynet.filecollect.notification.model.entity.MessageType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateMessageTemplateRequest(
    @field:NotBlank
    val name: String,
    
    @field:NotBlank
    val title: String,
    
    @field:NotBlank
    val content: String,
    
    @field:NotNull
    val type: MessageType
)

data class MessageTemplateResponse(
    val id: Long,
    val name: String,
    val title: String,
    val content: String,
    val type: MessageType,
    val createdAt: String,
    val updatedAt: String
) 