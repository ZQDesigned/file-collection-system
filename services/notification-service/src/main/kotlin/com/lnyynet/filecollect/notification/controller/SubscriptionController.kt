package com.lnyynet.filecollect.notification.controller

import com.lnyynet.filecollect.notification.model.dto.*
import com.lnyynet.filecollect.notification.model.entity.MessageType
import com.lnyynet.filecollect.notification.service.SubscriptionService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/subscriptions")
class SubscriptionController(
    private val subscriptionService: SubscriptionService
) {
    
    @GetMapping
    fun listSubscriptions(@AuthenticationPrincipal jwt: Jwt): List<SubscriptionResponse> {
        return subscriptionService.listSubscriptions(jwt.subject)
    }
    
    @PutMapping("/{type}")
    fun updateSubscription(
        @AuthenticationPrincipal jwt: Jwt,
        @PathVariable type: MessageType,
        @Valid @RequestBody request: UpdateSubscriptionRequest
    ): SubscriptionResponse {
        return subscriptionService.updateSubscription(jwt.subject, type, request)
    }
} 