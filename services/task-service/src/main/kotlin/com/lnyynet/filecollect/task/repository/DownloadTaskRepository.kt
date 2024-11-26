package com.lnyynet.filecollect.task.repository

import com.lnyynet.filecollect.task.model.entity.DownloadTask
import com.lnyynet.filecollect.task.model.entity.DownloadTaskStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DownloadTaskRepository : JpaRepository<DownloadTask, Long> {
    fun findByStatus(status: DownloadTaskStatus, pageable: Pageable): Page<DownloadTask>
    fun findByCreatedBy(createdBy: String, pageable: Pageable): Page<DownloadTask>
} 