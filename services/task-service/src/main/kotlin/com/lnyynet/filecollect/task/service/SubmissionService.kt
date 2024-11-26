package com.lnyynet.filecollect.task.service

import com.lnyynet.filecollect.task.model.dto.CreateSubmissionRequest
import com.lnyynet.filecollect.task.model.dto.SubmissionResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.multipart.MultipartFile
import java.io.InputStream

interface SubmissionService {
    fun createSubmission(
        taskId: Long,
        request: CreateSubmissionRequest,
        files: List<MultipartFile>
    ): SubmissionResponse
    
    fun getSubmission(id: Long): SubmissionResponse
    fun listSubmissions(taskId: Long, pageable: Pageable, keyword: String? = null): Page<SubmissionResponse>
    fun deleteSubmission(id: Long)
    fun getSubmissionFile(submissionId: Long, fileName: String): Pair<InputStream, String>
} 