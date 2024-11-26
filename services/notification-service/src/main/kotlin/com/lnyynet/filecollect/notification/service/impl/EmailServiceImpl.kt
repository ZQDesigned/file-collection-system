package com.lnyynet.filecollect.notification.service.impl

import com.lnyynet.filecollect.common.exception.ApiException
import com.lnyynet.filecollect.common.exception.ErrorCode
import com.lnyynet.filecollect.notification.model.dto.*
import com.lnyynet.filecollect.notification.model.entity.EmailLog
import com.lnyynet.filecollect.notification.model.entity.EmailStatus
import com.lnyynet.filecollect.notification.model.entity.EmailTemplate
import com.lnyynet.filecollect.notification.repository.EmailLogRepository
import com.lnyynet.filecollect.notification.repository.EmailTemplateRepository
import com.lnyynet.filecollect.notification.service.EmailService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class EmailServiceImpl(
    private val emailTemplateRepository: EmailTemplateRepository,
    private val emailLogRepository: EmailLogRepository,
    private val mailSender: JavaMailSender,
    private val rabbitTemplate: RabbitTemplate
) : EmailService {

    override fun sendEmail(request: EmailRequest) {
        val template = request.templateId?.let { findTemplateById(it) }
        val content = if (template != null) {
            processTemplate(template.content, request.variables)
        } else {
            request.content
        }
        
        val subject = if (template != null) {
            processTemplate(template.subject, request.variables)
        } else {
            request.subject
        }
        
        request.to.forEach { recipient ->
            val log = EmailLog(
                recipient = recipient,
                subject = subject,
                content = content,
                status = EmailStatus.PENDING
            )
            emailLogRepository.save(log)
            
            rabbitTemplate.convertAndSend(
                "email.exchange",
                "email.send",
                EmailMessage(log.id!!, recipient, subject, content, template?.isHtml ?: request.isHtml)
            )
        }
    }

    @Transactional
    override fun createTemplate(request: EmailTemplateRequest): EmailTemplateResponse {
        val template = EmailTemplate(
            name = request.name,
            subject = request.subject,
            content = request.content,
            isHtml = request.isHtml
        )
        return emailTemplateRepository.save(template).toResponse()
    }

    @Transactional
    override fun updateTemplate(id: Long, request: EmailTemplateRequest): EmailTemplateResponse {
        val template = findTemplateById(id)
        
        template.apply {
            name = request.name
            subject = request.subject
            content = request.content
            isHtml = request.isHtml
            updatedAt = LocalDateTime.now()
        }
        
        return emailTemplateRepository.save(template).toResponse()
    }

    @Transactional
    override fun deleteTemplate(id: Long) {
        val template = findTemplateById(id)
        emailTemplateRepository.delete(template)
    }

    override fun getTemplate(id: Long): EmailTemplateResponse {
        return findTemplateById(id).toResponse()
    }

    override fun listTemplates(pageable: Pageable): Page<EmailTemplateResponse> {
        return emailTemplateRepository.findAll(pageable).map { it.toResponse() }
    }
    
    private fun findTemplateById(id: Long): EmailTemplate {
        return emailTemplateRepository.findById(id)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "邮件模板不存在") }
    }
    
    private fun processTemplate(template: String, variables: Map<String, String>): String {
        var result = template
        variables.forEach { (key, value) ->
            result = result.replace("{{$key}}", value)
        }
        return result
    }
    
    private fun EmailTemplate.toResponse() = EmailTemplateResponse(
        id = id!!,
        name = name,
        subject = subject,
        content = content,
        isHtml = isHtml,
        createdAt = createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        updatedAt = updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
}

data class EmailMessage(
    val logId: Long,
    val to: String,
    val subject: String,
    val content: String,
    val isHtml: Boolean
) 