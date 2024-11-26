package com.lnyynet.filecollect.notification.controller

import com.lnyynet.filecollect.notification.model.dto.MessageResponse
import com.lnyynet.filecollect.notification.model.dto.UpdateMessageStatusRequest
import com.lnyynet.filecollect.notification.model.entity.MessageStatus
import com.lnyynet.filecollect.notification.service.MessageService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.annotation.SendToUser
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Controller

@Controller
class WebSocketController(
    private val messageService: MessageService
) {
    
    @MessageMapping("/messages/read")
    @SendToUser("/queue/messages")
    fun markMessageAsRead(
        @Payload messageId: Long,
        @AuthenticationPrincipal jwt: Jwt
    ): MessageResponse {
        return messageService.updateMessageStatus(messageId, UpdateMessageStatusRequest(MessageStatus.READ))
    }
    
    @MessageMapping("/messages/archive")
    @SendToUser("/queue/messages")
    fun archiveMessage(
        @Payload messageId: Long,
        @AuthenticationPrincipal jwt: Jwt
    ): MessageResponse {
        return messageService.updateMessageStatus(messageId, UpdateMessageStatusRequest(MessageStatus.ARCHIVED))
    }
} 