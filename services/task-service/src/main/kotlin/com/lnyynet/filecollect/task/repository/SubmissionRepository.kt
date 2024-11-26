package com.lnyynet.filecollect.task.repository

import com.lnyynet.filecollect.task.model.entity.Submission
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubmissionRepository : JpaRepository<Submission, Long> {
    fun findByTaskId(taskId: Long, pageable: Pageable): Page<Submission>
    fun findByTaskIdAndFormDataContaining(taskId: Long, keyword: String, pageable: Pageable): Page<Submission>
} 