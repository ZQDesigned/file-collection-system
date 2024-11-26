package com.lnyynet.filecollect.notification.service

import com.lnyynet.filecollect.notification.model.dto.SubscriptionResponse
import com.lnyynet.filecollect.notification.model.dto.UpdateSubscriptionRequest
import com.lnyynet.filecollect.notification.model.entity.MessageType

interface SubscriptionService {
    fun listSubscriptions(userId: String): List<SubscriptionResponse>
    fun updateSubscription(userId: String, type: MessageType, request: UpdateSubscriptionRequest): SubscriptionResponse
    fun shouldNotify(userId: String, type: MessageType): Boolean
} 