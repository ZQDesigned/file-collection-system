package com.lnyynet.filecollect.notification.model.dto

import com.lnyynet.filecollect.notification.model.entity.MessageType
import jakarta.validation.constraints.NotNull

data class UpdateSubscriptionRequest(
    @field:NotNull
    val enabled: Boolean
)

data class SubscriptionResponse(
    val id: Long,
    val userId: String,
    val type: MessageType,
    val enabled: Boolean,
    val createdAt: String,
    val updatedAt: String
) 