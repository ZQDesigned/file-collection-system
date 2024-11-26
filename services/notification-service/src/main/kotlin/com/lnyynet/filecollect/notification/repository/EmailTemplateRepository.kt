package com.lnyynet.filecollect.notification.repository

import com.lnyynet.filecollect.notification.model.entity.EmailTemplate
import org.springframework.data.jpa.repository.JpaRepository

interface EmailTemplateRepository : JpaRepository<EmailTemplate, Long> 