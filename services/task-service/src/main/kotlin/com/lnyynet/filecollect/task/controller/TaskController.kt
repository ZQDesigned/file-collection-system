package com.lnyynet.filecollect.task.controller

import com.lnyynet.filecollect.common.model.Response
import com.lnyynet.filecollect.task.model.dto.*
import com.lnyynet.filecollect.task.service.TaskService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(
    private val taskService: TaskService
) {
    @PostMapping
    fun createTask(
        @Valid @RequestBody request: CreateTaskRequest,
        authentication: Authentication
    ): Response<TaskResponse> {
        return Response.success(taskService.createTask(request, authentication.name))
    }

    @GetMapping("/{id}")
    fun getTask(@PathVariable id: Long): Response<TaskDetailResponse> {
        return Response.success(taskService.getTask(id))
    }

    @GetMapping
    fun listTasks(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) status: String?
    ): Response<Page<TaskResponse>> {
        return Response.success(taskService.listTasks(PageRequest.of(page, size), status))
    }

    @PutMapping("/{id}")
    fun updateTask(
        @PathVariable id: Long,
        @Valid @RequestBody request: CreateTaskRequest
    ): Response<TaskResponse> {
        return Response.success(taskService.updateTask(id, request))
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): Response<Nothing> {
        taskService.deleteTask(id)
        return Response.success()
    }
} 