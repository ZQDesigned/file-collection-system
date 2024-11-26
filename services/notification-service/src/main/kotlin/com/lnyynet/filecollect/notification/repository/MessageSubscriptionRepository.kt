package com.lnyynet.filecollect.notification.repository

import com.lnyynet.filecollect.notification.model.entity.MessageSubscription
import com.lnyynet.filecollect.notification.model.entity.MessageType
import org.springframework.data.jpa.repository.JpaRepository

interface MessageSubscriptionRepository : JpaRepository<MessageSubscription, Long> {
    fun findByUserIdAndType(userId: String, type: MessageType): MessageSubscription?
    fun findByUserIdAndEnabledTrue(userId: String): List<MessageSubscription>
} 