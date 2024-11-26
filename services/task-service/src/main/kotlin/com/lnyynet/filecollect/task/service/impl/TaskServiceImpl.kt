package com.lnyynet.filecollect.task.service.impl

import com.fasterxml.jackson.databind.ObjectMapper
import com.lnyynet.filecollect.common.exception.ApiException
import com.lnyynet.filecollect.common.exception.ErrorCode
import com.lnyynet.filecollect.task.model.dto.*
import com.lnyynet.filecollect.task.model.entity.Task
import com.lnyynet.filecollect.task.repository.TaskRepository
import com.lnyynet.filecollect.task.service.TaskService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TaskServiceImpl(
    private val taskRepository: TaskRepository,
    private val objectMapper: ObjectMapper
) : TaskService {

    @Transactional
    override fun createTask(request: CreateTaskRequest, createdBy: String): TaskResponse {
        val task = Task(
            title = request.title,
            description = request.description,
            deadline = request.deadline,
            fileTypes = request.fileTypes,
            maxFiles = request.maxFiles,
            formFields = objectMapper.writeValueAsString(request.formFields),
            createdBy = createdBy
        )
        
        return taskRepository.save(task).toResponse()
    }

    override fun getTask(id: Long): TaskDetailResponse {
        val task = findTaskById(id)
        return task.toDetailResponse()
    }

    override fun listTasks(pageable: Pageable, status: String?): Page<TaskResponse> {
        val tasks = when (status) {
            "active" -> taskRepository.findActiveTasksPage(pageable)
            else -> taskRepository.findAll(pageable)
        }
        return tasks.map { it.toResponse() }
    }

    @Transactional
    override fun updateTask(id: Long, request: CreateTaskRequest): TaskResponse {
        val task = findTaskById(id)
        
        task.apply {
            title = request.title
            description = request.description
            deadline = request.deadline
            fileTypes = request.fileTypes
            maxFiles = request.maxFiles
            formFields = objectMapper.writeValueAsString(request.formFields)
            updatedAt = LocalDateTime.now()
        }
        
        return taskRepository.save(task).toResponse()
    }

    @Transactional
    override fun deleteTask(id: Long) {
        val task = findTaskById(id)
        taskRepository.delete(task)
    }

    private fun findTaskById(id: Long): Task {
        return taskRepository.findById(id)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "任务不存在") }
    }

    private fun Task.toResponse() = TaskResponse(
        id = id!!,
        title = title,
        description = description,
        deadline = deadline,
        fileTypes = fileTypes,
        maxFiles = maxFiles,
        formFields = objectMapper.readValue(formFields, objectMapper.typeFactory.constructCollectionType(List::class.java, FormField::class.java)),
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt,
        submissionCount = taskRepository.countSubmissionsByTaskId(id!!)
    )

    private fun Task.toDetailResponse() = TaskDetailResponse(
        id = id!!,
        title = title,
        description = description,
        deadline = deadline,
        fileTypes = fileTypes,
        maxFiles = maxFiles,
        formFields = objectMapper.readValue(formFields, objectMapper.typeFactory.constructCollectionType(List::class.java, FormField::class.java)),
        createdBy = createdBy,
        createdAt = createdAt,
        updatedAt = updatedAt,
        submissionCount = taskRepository.countSubmissionsByTaskId(id!!)
    )
} 