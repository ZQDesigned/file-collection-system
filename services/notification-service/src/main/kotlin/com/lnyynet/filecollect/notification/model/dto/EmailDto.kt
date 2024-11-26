package com.lnyynet.filecollect.notification.model.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class EmailRequest(
    @field:NotEmpty
    val to: List<@Email String>,
    
    @field:NotBlank
    val subject: String,
    
    @field:NotBlank
    val content: String,
    
    val templateId: Long? = null,
    
    val variables: Map<String, String> = emptyMap(),
    
    val isHtml: Boolean = false
)

data class EmailTemplateRequest(
    @field:NotBlank
    val name: String,
    
    @field:NotBlank
    val subject: String,
    
    @field:NotBlank
    val content: String,
    
    val isHtml: Boolean = false
)

data class EmailTemplateResponse(
    val id: Long,
    val name: String,
    val subject: String,
    val content: String,
    val isHtml: Boolean,
    val createdAt: String,
    val updatedAt: String
) 