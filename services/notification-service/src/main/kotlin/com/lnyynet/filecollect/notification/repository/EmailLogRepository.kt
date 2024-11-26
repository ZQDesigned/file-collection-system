package com.lnyynet.filecollect.notification.repository

import com.lnyynet.filecollect.notification.model.entity.EmailLog
import com.lnyynet.filecollect.notification.model.entity.EmailStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface EmailLogRepository : JpaRepository<EmailLog, Long> {
    fun findByStatusAndCreatedAtBefore(status: EmailStatus, time: LocalDateTime): List<EmailLog>
} 