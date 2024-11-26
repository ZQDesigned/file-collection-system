package com.lnyynet.filecollect.notification.repository

import com.lnyynet.filecollect.notification.model.entity.MessageTemplate
import com.lnyynet.filecollect.notification.model.entity.MessageType
import org.springframework.data.jpa.repository.JpaRepository

interface MessageTemplateRepository : JpaRepository<MessageTemplate, Long> {
    fun findByType(type: MessageType): List<MessageTemplate>
    fun findByName(name: String): MessageTemplate?
} 