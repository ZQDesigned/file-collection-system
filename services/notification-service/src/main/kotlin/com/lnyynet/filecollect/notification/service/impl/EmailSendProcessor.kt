package com.lnyynet.filecollect.notification.service.impl

import com.lnyynet.filecollect.notification.model.entity.EmailStatus
import com.lnyynet.filecollect.notification.repository.EmailLogRepository
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Component
class EmailSendProcessor(
    private val emailLogRepository: EmailLogRepository,
    private val mailSender: JavaMailSender
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    
    @RabbitListener(queues = ["email.send.queue"])
    @Transactional
    fun processEmail(message: EmailMessage) {
        val log = emailLogRepository.findById(message.logId).get()
        
        try {
            mailSender.createMimeMessage().let { mimeMessage ->
                MimeMessageHelper(mimeMessage, true).apply {
                    setTo(message.to)
                    setSubject(message.subject)
                    setText(message.content, message.isHtml)
                }
                mailSender.send(mimeMessage)
            }
            
            log.status = EmailStatus.SENT
            log.updatedAt = LocalDateTime.now()
            emailLogRepository.save(log)
            
        } catch (e: Exception) {
            logger.error("Failed to send email to ${message.to}", e)
            log.status = EmailStatus.FAILED
            log.error = e.message
            log.updatedAt = LocalDateTime.now()
            emailLogRepository.save(log)
        }
    }
} 