package com.lnyynet.filecollect.notification.service.impl

import com.lnyynet.filecollect.common.exception.ApiException
import com.lnyynet.filecollect.common.exception.ErrorCode
import com.lnyynet.filecollect.notification.controller.BatchDeleteRequest
import com.lnyynet.filecollect.notification.controller.BatchUpdateStatusRequest
import com.lnyynet.filecollect.notification.controller.TagInfo
import com.lnyynet.filecollect.notification.controller.UpdateTagsRequest
import com.lnyynet.filecollect.notification.model.dto.*
import com.lnyynet.filecollect.notification.model.entity.*
import com.lnyynet.filecollect.notification.repository.MessageRepository
import com.lnyynet.filecollect.notification.repository.MessageTemplateRepository
import com.lnyynet.filecollect.notification.service.CreateMessageFromTemplateRequest
import com.lnyynet.filecollect.notification.service.MessageNotifier
import com.lnyynet.filecollect.notification.service.MessageService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class MessageServiceImpl(
    private val messageRepository: MessageRepository,
    private val messageTemplateRepository: MessageTemplateRepository,
    private val messageNotifier: MessageNotifier
) : MessageService {

    @Transactional
    override fun createMessage(request: CreateMessageRequest): MessageResponse {
        val message = Message(
            userId = request.userId,
            title = request.title,
            content = request.content,
            type = request.type
        )
        
        val savedMessage = messageRepository.save(message)
        messageNotifier.notifyNewMessage(savedMessage)
        return savedMessage.toResponse()
    }

    override fun getMessage(id: Long): MessageResponse {
        return findMessageById(id).toResponse()
    }

    override fun listMessages(userId: String, status: MessageStatus?, pageable: Pageable): Page<MessageResponse> {
        val messages = if (status != null) {
            messageRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, status, pageable)
        } else {
            messageRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
        }
        return messages.map { it.toResponse() }
    }

    @Transactional
    override fun updateMessageStatus(id: Long, request: UpdateMessageStatusRequest): MessageResponse {
        val message = findMessageById(id)
        message.status = request.status
        message.updatedAt = LocalDateTime.now()
        return messageRepository.save(message).toResponse()
    }

    @Transactional
    override fun deleteMessage(id: Long) {
        val message = findMessageById(id)
        messageRepository.delete(message)
    }

    override fun getMessageStats(userId: String): MessageStats {
        return MessageStats(
            totalCount = messageRepository.countByUserId(userId),
            unreadCount = messageRepository.countByUserIdAndStatus(userId, MessageStatus.UNREAD)
        )
    }
    
    @Transactional
    override fun createMessageFromTemplate(request: CreateMessageFromTemplateRequest): MessageResponse {
        val template = messageTemplateRepository.findById(request.templateId)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "消息模板不存在") }
            
        val title = processTemplate(template.title, request.variables)
        val content = processTemplate(template.content, request.variables)
        
        val message = Message(
            userId = request.userId,
            title = title,
            content = content,
            type = template.type
        )
        
        val savedMessage = messageRepository.save(message)
        messageNotifier.notifyNewMessage(savedMessage)
        return savedMessage.toResponse()
    }

    @Transactional
    override fun createTemplate(request: CreateMessageTemplateRequest): MessageTemplateResponse {
        if (messageTemplateRepository.findByName(request.name) != null) {
            throw ApiException(ErrorCode.VALIDATION_ERROR, "模板名称已存在")
        }
        
        val template = MessageTemplate(
            name = request.name,
            title = request.title,
            content = request.content,
            type = request.type
        )
        
        return messageTemplateRepository.save(template).toResponse()
    }

    @Transactional
    override fun updateTemplate(id: Long, request: CreateMessageTemplateRequest): MessageTemplateResponse {
        val template = findTemplateById(id)
        
        val existingTemplate = messageTemplateRepository.findByName(request.name)
        if (existingTemplate != null && existingTemplate.id != id) {
            throw ApiException(ErrorCode.VALIDATION_ERROR, "模板名称已存在")
        }
        
        template.apply {
            name = request.name
            title = request.title
            content = request.content
            type = request.type
            updatedAt = LocalDateTime.now()
        }
        
        return messageTemplateRepository.save(template).toResponse()
    }

    @Transactional
    override fun deleteTemplate(id: Long) {
        val template = findTemplateById(id)
        messageTemplateRepository.delete(template)
    }

    override fun getTemplate(id: Long): MessageTemplateResponse {
        return findTemplateById(id).toResponse()
    }

    override fun listTemplates(type: MessageType?): List<MessageTemplateResponse> {
        val templates = if (type != null) {
            messageTemplateRepository.findByType(type)
        } else {
            messageTemplateRepository.findAll()
        }
        return templates.map { it.toResponse() }
    }
    
    private fun findMessageById(id: Long): Message {
        return messageRepository.findById(id)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "消息不存在") }
    }
    
    private fun findTemplateById(id: Long): MessageTemplate {
        return messageTemplateRepository.findById(id)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "消息模板不存在") }
    }
    
    private fun processTemplate(template: String, variables: Map<String, String>): String {
        var result = template
        variables.forEach { (key, value) ->
            result = result.replace("{{$key}}", value)
        }
        return result
    }
    
    private fun Message.toResponse() = MessageResponse(
        id = id!!,
        userId = userId,
        title = title,
        content = content,
        type = type,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
    
    private fun MessageTemplate.toResponse() = MessageTemplateResponse(
        id = id!!,
        name = name,
        title = title,
        content = content,
        type = type,
        createdAt = createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        updatedAt = updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )

    @Transactional
    override fun batchUpdateStatus(request: BatchUpdateStatusRequest): List<MessageResponse> {
        val messages = messageRepository.findAllById(request.messageIds)
        messages.forEach { message ->
            message.status = request.status
            message.updatedAt = LocalDateTime.now()
        }
        return messageRepository.saveAll(messages).map { it.toResponse() }
    }

    @Transactional
    override fun batchDelete(request: BatchDeleteRequest) {
        messageRepository.deleteAllById(request.messageIds)
    }

    override fun searchMessages(
        userId: String,
        keyword: String,
        status: MessageStatus?,
        type: MessageType?,
        tags: List<String>?,
        pageable: Pageable
    ): Page<MessageResponse> {
        return messageRepository.searchMessages(
            userId = userId,
            status = status,
            type = type,
            tags = tags,
            keyword = keyword,
            pageable = pageable
        ).map { it.toResponse() }
    }
    
    @Transactional
    override fun updateTags(id: Long, request: UpdateTagsRequest): MessageResponse {
        val message = findMessageById(id)
        message.tags = request.tags.toMutableSet()
        message.updatedAt = LocalDateTime.now()
        return messageRepository.save(message).toResponse()
    }
    
    override fun listTags(userId: String): List<TagInfo> {
        return messageRepository.countMessagesByTag(userId)
            .map { TagInfo(it.getTag(), it.getCount()) }
    }
} 