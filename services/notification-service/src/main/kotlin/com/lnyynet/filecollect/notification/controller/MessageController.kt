package com.lnyynet.filecollect.notification.controller

import com.lnyynet.filecollect.notification.model.dto.*
import com.lnyynet.filecollect.notification.model.entity.MessageStatus
import com.lnyynet.filecollect.notification.model.entity.MessageType
import com.lnyynet.filecollect.notification.service.MessageService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/messages")
class MessageController(
    private val messageService: MessageService
) {
    
    @PostMapping
    fun createMessage(@Valid @RequestBody request: CreateMessageRequest): MessageResponse {
        return messageService.createMessage(request)
    }
    
    @GetMapping("/{id}")
    fun getMessage(@PathVariable id: Long): MessageResponse {
        return messageService.getMessage(id)
    }
    
    @GetMapping
    fun listMessages(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestParam status: MessageStatus?,
        pageable: Pageable
    ): Page<MessageResponse> {
        return messageService.listMessages(jwt.subject, status, pageable)
    }
    
    @PutMapping("/{id}/status")
    fun updateMessageStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateMessageStatusRequest
    ): MessageResponse {
        return messageService.updateMessageStatus(id, request)
    }
    
    @DeleteMapping("/{id}")
    fun deleteMessage(@PathVariable id: Long) {
        messageService.deleteMessage(id)
    }
    
    @GetMapping("/stats")
    fun getMessageStats(@AuthenticationPrincipal jwt: Jwt): MessageStats {
        return messageService.getMessageStats(jwt.subject)
    }
    
    @PostMapping("/batch/status")
    fun batchUpdateStatus(
        @Valid @RequestBody request: BatchUpdateStatusRequest
    ): List<MessageResponse> {
        return messageService.batchUpdateStatus(request)
    }
    
    @DeleteMapping("/batch")
    fun batchDelete(@RequestBody request: BatchDeleteRequest) {
        messageService.batchDelete(request)
    }
    
    @GetMapping("/search")
    fun searchMessages(
        @AuthenticationPrincipal jwt: Jwt,
        @RequestParam keyword: String,
        @RequestParam status: MessageStatus?,
        @RequestParam type: MessageType?,
        @RequestParam tags: List<String>?,
        pageable: Pageable
    ): Page<MessageResponse> {
        return messageService.searchMessages(
            userId = jwt.subject,
            keyword = keyword,
            status = status,
            type = type,
            tags = tags,
            pageable = pageable
        )
    }
    
    @PutMapping("/{id}/tags")
    fun updateMessageTags(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateTagsRequest
    ): MessageResponse {
        return messageService.updateTags(id, request)
    }
    
    @GetMapping("/tags")
    fun listTags(@AuthenticationPrincipal jwt: Jwt): List<TagInfo> {
        return messageService.listTags(jwt.subject)
    }
}

data class BatchUpdateStatusRequest(
    val messageIds: List<Long>,
    val status: MessageStatus
)

data class BatchDeleteRequest(
    val messageIds: List<Long>
)

data class UpdateTagsRequest(
    val tags: List<String>
)

data class TagInfo(
    val name: String,
    val count: Long
) 