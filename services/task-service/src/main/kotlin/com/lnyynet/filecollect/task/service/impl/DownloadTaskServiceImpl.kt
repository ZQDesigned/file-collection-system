package com.lnyynet.filecollect.task.service.impl

import com.lnyynet.filecollect.common.exception.ApiException
import com.lnyynet.filecollect.common.exception.ErrorCode
import com.lnyynet.filecollect.task.model.dto.*
import com.lnyynet.filecollect.task.model.entity.DownloadTask
import com.lnyynet.filecollect.task.model.entity.DownloadTaskStatus
import com.lnyynet.filecollect.task.repository.DownloadTaskRepository
import com.lnyynet.filecollect.task.repository.TaskRepository
import com.lnyynet.filecollect.task.service.DownloadTaskService
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DownloadTaskServiceImpl(
    private val downloadTaskRepository: DownloadTaskRepository,
    private val taskRepository: TaskRepository,
    private val rabbitTemplate: RabbitTemplate
) : DownloadTaskService {

    @Transactional
    override fun createDownloadTask(
        taskId: Long,
        request: CreateDownloadTaskRequest,
        createdBy: String
    ): DownloadTaskResponse {
        val task = taskRepository.findById(taskId)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "任务不存在") }
            
        val downloadTask = downloadTaskRepository.save(
            DownloadTask(
                task = task,
                submissionId = request.submissionId,
                createdBy = createdBy
            )
        )
        
        // 发送消息到队列
        rabbitTemplate.convertAndSend(
            "download-tasks",
            "download-task.created",
            DownloadTaskMessage(
                downloadTaskId = downloadTask.id!!,
                settings = request.settings
            )
        )
        
        return downloadTask.toResponse()
    }

    override fun getDownloadTask(id: Long): DownloadTaskResponse {
        return findDownloadTaskById(id).toResponse()
    }

    override fun listDownloadTasks(
        pageable: Pageable,
        status: DownloadTaskStatus?
    ): Page<DownloadTaskResponse> {
        val tasks = if (status != null) {
            downloadTaskRepository.findByStatus(status, pageable)
        } else {
            downloadTaskRepository.findAll(pageable)
        }
        return tasks.map { it.toResponse() }
    }

    @Transactional
    override fun retryDownloadTask(id: Long): DownloadTaskResponse {
        val downloadTask = findDownloadTaskById(id)
        
        if (downloadTask.status != DownloadTaskStatus.FAILED) {
            throw ApiException(ErrorCode.VALIDATION_ERROR, "只能重试失败的下载任务")
        }
        
        downloadTask.status = DownloadTaskStatus.PENDING
        downloadTask.url = null
        
        val savedTask = downloadTaskRepository.save(downloadTask)
        
        // 重新发送消息到队列
        rabbitTemplate.convertAndSend(
            "download-tasks",
            "download-task.created",
            DownloadTaskMessage(
                downloadTaskId = savedTask.id!!,
                settings = null
            )
        )
        
        return savedTask.toResponse()
    }

    @Transactional
    override fun deleteDownloadTask(id: Long) {
        val downloadTask = findDownloadTaskById(id)
        downloadTaskRepository.delete(downloadTask)
    }

    private fun findDownloadTaskById(id: Long): DownloadTask {
        return downloadTaskRepository.findById(id)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "下载任务不存在") }
    }

    private fun DownloadTask.toResponse() = DownloadTaskResponse(
        id = id!!,
        taskId = task.id!!,
        type = if (submissionId != null) DownloadType.SINGLE else DownloadType.ALL,
        submissionId = submissionId,
        status = status,
        url = url,
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

data class DownloadTaskMessage(
    val downloadTaskId: Long,
    val settings: DownloadSettings?
) 