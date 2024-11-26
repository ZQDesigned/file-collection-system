package com.lnyynet.filecollect.task.controller

import com.lnyynet.filecollect.common.model.Response
import com.lnyynet.filecollect.task.model.dto.CreateDownloadTaskRequest
import com.lnyynet.filecollect.task.model.dto.DownloadTaskResponse
import com.lnyynet.filecollect.task.model.entity.DownloadTaskStatus
import com.lnyynet.filecollect.task.service.DownloadTaskService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/downloads")
class DownloadTaskController(
    private val downloadTaskService: DownloadTaskService
) {
    @PostMapping
    fun createDownloadTask(
        @PathVariable taskId: Long,
        @Valid @RequestBody request: CreateDownloadTaskRequest,
        authentication: Authentication
    ): Response<DownloadTaskResponse> {
        return Response.success(
            downloadTaskService.createDownloadTask(taskId, request, authentication.name)
        )
    }

    @GetMapping
    fun listDownloadTasks(
        @PathVariable taskId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) status: DownloadTaskStatus?
    ): Response<Page<DownloadTaskResponse>> {
        return Response.success(
            downloadTaskService.listDownloadTasks(PageRequest.of(page, size), status)
        )
    }

    @PostMapping("/{downloadTaskId}/retry")
    fun retryDownloadTask(
        @PathVariable taskId: Long,
        @PathVariable downloadTaskId: Long
    ): Response<DownloadTaskResponse> {
        return Response.success(downloadTaskService.retryDownloadTask(downloadTaskId))
    }

    @DeleteMapping("/{downloadTaskId}")
    fun deleteDownloadTask(
        @PathVariable taskId: Long,
        @PathVariable downloadTaskId: Long
    ): Response<Nothing> {
        downloadTaskService.deleteDownloadTask(downloadTaskId)
        return Response.success()
    }
} 