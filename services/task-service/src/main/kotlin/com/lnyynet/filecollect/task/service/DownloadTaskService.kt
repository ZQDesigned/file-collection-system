package com.lnyynet.filecollect.task.service

import com.lnyynet.filecollect.task.model.dto.CreateDownloadTaskRequest
import com.lnyynet.filecollect.task.model.dto.DownloadTaskResponse
import com.lnyynet.filecollect.task.model.entity.DownloadTaskStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface DownloadTaskService {
    fun createDownloadTask(taskId: Long, request: CreateDownloadTaskRequest, createdBy: String): DownloadTaskResponse
    fun getDownloadTask(id: Long): DownloadTaskResponse
    fun listDownloadTasks(pageable: Pageable, status: DownloadTaskStatus? = null): Page<DownloadTaskResponse>
    fun retryDownloadTask(id: Long): DownloadTaskResponse
    fun deleteDownloadTask(id: Long)
} 