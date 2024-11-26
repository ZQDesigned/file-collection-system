package com.lnyynet.filecollect.notification.service.impl

import com.lnyynet.filecollect.notification.model.dto.SubscriptionResponse
import com.lnyynet.filecollect.notification.model.dto.UpdateSubscriptionRequest
import com.lnyynet.filecollect.notification.model.entity.MessageSubscription
import com.lnyynet.filecollect.notification.model.entity.MessageType
import com.lnyynet.filecollect.notification.repository.MessageSubscriptionRepository
import com.lnyynet.filecollect.notification.service.SubscriptionService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class SubscriptionServiceImpl(
    private val subscriptionRepository: MessageSubscriptionRepository
) : SubscriptionService {

    override fun listSubscriptions(userId: String): List<SubscriptionResponse> {
        val subscriptions = subscriptionRepository.findByUserIdAndEnabledTrue(userId)
        return subscriptions.map { it.toResponse() }
    }

    @Transactional
    override fun updateSubscription(
        userId: String,
        type: MessageType,
        request: UpdateSubscriptionRequest
    ): SubscriptionResponse {
        val subscription = subscriptionRepository.findByUserIdAndType(userId, type)
            ?: MessageSubscription(userId = userId, type = type)
            
        subscription.apply {
            enabled = request.enabled
            updatedAt = LocalDateTime.now()
        }
        
        return subscriptionRepository.save(subscription).toResponse()
    }

    override fun shouldNotify(userId: String, type: MessageType): Boolean {
        val subscription = subscriptionRepository.findByUserIdAndType(userId, type)
        return subscription?.enabled ?: true
    }
    
    private fun MessageSubscription.toResponse() = SubscriptionResponse(
        id = id!!,
        userId = userId,
        type = type,
        enabled = enabled,
        createdAt = createdAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
        updatedAt = updatedAt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    )
} 