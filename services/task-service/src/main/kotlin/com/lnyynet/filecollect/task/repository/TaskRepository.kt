package com.lnyynet.filecollect.task.repository

import com.lnyynet.filecollect.task.model.entity.Task
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TaskRepository : JpaRepository<Task, Long> {
    @Query("SELECT t FROM Task t WHERE t.deadline > CURRENT_TIMESTAMP")
    fun findActiveTasksPage(pageable: org.springframework.data.domain.Pageable): org.springframework.data.domain.Page<Task>
    
    @Query("SELECT COUNT(s) FROM Submission s WHERE s.task.id = :taskId")
    fun countSubmissionsByTaskId(taskId: Long): Int
} 