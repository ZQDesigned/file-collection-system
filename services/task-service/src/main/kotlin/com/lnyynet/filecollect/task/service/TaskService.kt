package com.lnyynet.filecollect.task.service

import com.lnyynet.filecollect.task.model.dto.*
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TaskService {
    fun createTask(request: CreateTaskRequest, createdBy: String): TaskResponse
    fun getTask(id: Long): TaskDetailResponse
    fun listTasks(pageable: Pageable, status: String? = null): Page<TaskResponse>
    fun updateTask(id: Long, request: CreateTaskRequest): TaskResponse
    fun deleteTask(id: Long)
} 