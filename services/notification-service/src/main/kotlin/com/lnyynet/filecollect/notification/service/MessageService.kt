package com.lnyynet.filecollect.notification.service

import com.lnyynet.filecollect.notification.controller.BatchDeleteRequest
import com.lnyynet.filecollect.notification.controller.BatchUpdateStatusRequest
import com.lnyynet.filecollect.notification.controller.TagInfo
import com.lnyynet.filecollect.notification.controller.UpdateTagsRequest
import com.lnyynet.filecollect.notification.model.dto.*
import com.lnyynet.filecollect.notification.model.entity.MessageStatus
import com.lnyynet.filecollect.notification.model.entity.MessageType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MessageService {
    fun createMessage(request: CreateMessageRequest): MessageResponse
    fun createMessageFromTemplate(request: CreateMessageFromTemplateRequest): MessageResponse
    fun getMessage(id: Long): MessageResponse
    fun listMessages(userId: String, status: MessageStatus?, pageable: Pageable): Page<MessageResponse>
    fun updateMessageStatus(id: Long, request: UpdateMessageStatusRequest): MessageResponse
    fun deleteMessage(id: Long)
    fun getMessageStats(userId: String): MessageStats
    fun createTemplate(request: CreateMessageTemplateRequest): MessageTemplateResponse
    fun updateTemplate(id: Long, request: CreateMessageTemplateRequest): MessageTemplateResponse
    fun deleteTemplate(id: Long)
    fun getTemplate(id: Long): MessageTemplateResponse
    fun listTemplates(type: MessageType?): List<MessageTemplateResponse>
    fun batchUpdateStatus(request: BatchUpdateStatusRequest): List<MessageResponse>
    fun batchDelete(request: BatchDeleteRequest)
    fun searchMessages(
        userId: String,
        keyword: String,
        status: MessageStatus?,
        type: MessageType?,
        tags: List<String>?,
        pageable: Pageable
    ): Page<MessageResponse>
    fun updateTags(id: Long, request: UpdateTagsRequest): MessageResponse
    fun listTags(userId: String): List<TagInfo>
}

data class CreateMessageFromTemplateRequest(
    val userId: String,
    val templateId: Long,
    val variables: Map<String, String>
) 