package com.lnyynet.filecollect.notification.service

import com.lnyynet.filecollect.notification.model.dto.EmailRequest
import com.lnyynet.filecollect.notification.model.dto.EmailTemplateRequest
import com.lnyynet.filecollect.notification.model.dto.EmailTemplateResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface EmailService {
    fun sendEmail(request: EmailRequest)
    fun createTemplate(request: EmailTemplateRequest): EmailTemplateResponse
    fun updateTemplate(id: Long, request: EmailTemplateRequest): EmailTemplateResponse
    fun deleteTemplate(id: Long)
    fun getTemplate(id: Long): EmailTemplateResponse
    fun listTemplates(pageable: Pageable): Page<EmailTemplateResponse>
} 