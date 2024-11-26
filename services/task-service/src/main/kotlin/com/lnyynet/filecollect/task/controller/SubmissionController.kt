package com.lnyynet.filecollect.task.controller

import com.lnyynet.filecollect.common.model.Response
import com.lnyynet.filecollect.task.model.dto.CreateSubmissionRequest
import com.lnyynet.filecollect.task.model.dto.SubmissionResponse
import com.lnyynet.filecollect.task.service.SubmissionService
import jakarta.validation.Valid
import org.springframework.core.io.InputStreamResource
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/tasks/{taskId}/submissions")
class SubmissionController(
    private val submissionService: SubmissionService
) {
    @PostMapping
    fun createSubmission(
        @PathVariable taskId: Long,
        @RequestPart("formData") @Valid request: CreateSubmissionRequest,
        @RequestPart("files") files: List<MultipartFile>
    ): Response<SubmissionResponse> {
        return Response.success(submissionService.createSubmission(taskId, request, files))
    }

    @GetMapping
    fun listSubmissions(
        @PathVariable taskId: Long,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) keyword: String?
    ): Response<Page<SubmissionResponse>> {
        return Response.success(
            submissionService.listSubmissions(taskId, PageRequest.of(page, size), keyword)
        )
    }

    @GetMapping("/{submissionId}")
    fun getSubmission(
        @PathVariable taskId: Long,
        @PathVariable submissionId: Long
    ): Response<SubmissionResponse> {
        return Response.success(submissionService.getSubmission(submissionId))
    }

    @GetMapping("/{submissionId}/files/{fileName}")
    fun downloadFile(
        @PathVariable taskId: Long,
        @PathVariable submissionId: Long,
        @PathVariable fileName: String
    ): ResponseEntity<InputStreamResource> {
        val (inputStream, contentType) = submissionService.getSubmissionFile(submissionId, fileName)
        
        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"$fileName\"")
            .body(InputStreamResource(inputStream))
    }

    @DeleteMapping("/{submissionId}")
    fun deleteSubmission(
        @PathVariable taskId: Long,
        @PathVariable submissionId: Long
    ): Response<Nothing> {
        submissionService.deleteSubmission(submissionId)
        return Response.success()
    }
} 