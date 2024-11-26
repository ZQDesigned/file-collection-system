package com.lnyynet.filecollect.notification.controller

import com.lnyynet.filecollect.notification.model.dto.*
import com.lnyynet.filecollect.notification.model.entity.MessageType
import com.lnyynet.filecollect.notification.service.CreateMessageFromTemplateRequest
import com.lnyynet.filecollect.notification.service.MessageService
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/message-templates")
class MessageTemplateController(
    private val messageService: MessageService
) {
    
    @PostMapping
    fun createTemplate(@Valid @RequestBody request: CreateMessageTemplateRequest): MessageTemplateResponse {
        return messageService.createTemplate(request)
    }
    
    @GetMapping("/{id}")
    fun getTemplate(@PathVariable id: Long): MessageTemplateResponse {
        return messageService.getTemplate(id)
    }
    
    @GetMapping
    fun listTemplates(@RequestParam type: MessageType?): List<MessageTemplateResponse> {
        return messageService.listTemplates(type)
    }
    
    @PutMapping("/{id}")
    fun updateTemplate(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateMessageTemplateRequest
    ): MessageTemplateResponse {
        return messageService.updateTemplate(id, request)
    }
    
    @DeleteMapping("/{id}")
    fun deleteTemplate(@PathVariable id: Long) {
        messageService.deleteTemplate(id)
    }
    
    @PostMapping("/{id}/send")
    fun sendMessageFromTemplate(
        @PathVariable id: Long,
        @Valid @RequestBody request: SendTemplateMessageRequest
    ): MessageResponse {
        return messageService.createMessageFromTemplate(
            CreateMessageFromTemplateRequest(
                userId = request.userId,
                templateId = id,
                variables = request.variables
            )
        )
    }
}

data class SendTemplateMessageRequest(
    val userId: String,
    val variables: Map<String, String>
) 