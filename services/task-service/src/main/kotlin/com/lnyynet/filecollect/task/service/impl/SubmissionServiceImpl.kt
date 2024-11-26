package com.lnyynet.filecollect.task.service.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.lnyynet.filecollect.common.exception.ApiException
import com.lnyynet.filecollect.common.exception.ErrorCode
import com.lnyynet.filecollect.task.client.FileServiceClient
import com.lnyynet.filecollect.task.model.dto.*
import com.lnyynet.filecollect.task.model.entity.Submission
import com.lnyynet.filecollect.task.model.entity.SubmissionFile
import com.lnyynet.filecollect.task.repository.SubmissionRepository
import com.lnyynet.filecollect.task.repository.TaskRepository
import com.lnyynet.filecollect.task.service.SubmissionService
import kotlinx.coroutines.runBlocking
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.time.LocalDateTime
import java.util.*

@Service
class SubmissionServiceImpl(
    private val submissionRepository: SubmissionRepository,
    private val taskRepository: TaskRepository,
    private val fileServiceClient: FileServiceClient,
    private val objectMapper: ObjectMapper
) : SubmissionService {

    private val formDataTypeRef = object : TypeReference<Map<String, String>>() {}

    @Transactional
    override fun createSubmission(
        taskId: Long,
        request: CreateSubmissionRequest,
        files: List<MultipartFile>
    ): SubmissionResponse = runBlocking {
        val task = taskRepository.findById(taskId)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "任务不存在") }
            
        if (LocalDateTime.now().isAfter(task.deadline)) {
            throw ApiException(ErrorCode.VALIDATION_ERROR, "任务已截止")
        }
        
        if (files.size > task.maxFiles) {
            throw ApiException(ErrorCode.VALIDATION_ERROR, "超出最大文件数量限制")
        }
        
        files.forEach { file ->
            if (!task.fileTypes.contains(file.contentType)) {
                throw ApiException(ErrorCode.VALIDATION_ERROR, "不支持的文件类型: ${file.contentType}")
            }
        }
        
        val submissionFiles = files.map { file ->
            val path = "submissions/${taskId}/${UUID.randomUUID()}/${file.originalFilename}"
            fileServiceClient.uploadFile(file, path)
            SubmissionFile(
                name = file.originalFilename ?: "unknown",
                size = file.size,
                type = file.contentType ?: "application/octet-stream",
                path = path
            )
        }.toSet()
        
        val submission = Submission(
            task = task,
            formData = objectMapper.writeValueAsString(request.formData),
            files = submissionFiles
        )
        
        submissionRepository.save(submission).toResponse()
    }

    override fun getSubmission(id: Long): SubmissionResponse {
        return findSubmissionById(id).toResponse()
    }

    override fun listSubmissions(
        taskId: Long,
        pageable: Pageable,
        keyword: String?
    ): Page<SubmissionResponse> {
        val submissions = if (keyword != null) {
            submissionRepository.findByTaskIdAndFormDataContaining(taskId, keyword, pageable)
        } else {
            submissionRepository.findByTaskId(taskId, pageable)
        }
        return submissions.map { it.toResponse() }
    }

    @Transactional
    override fun deleteSubmission(id: Long) = runBlocking {
        val submission = findSubmissionById(id)
        submission.files.forEach { file ->
            fileServiceClient.deleteFile(file.path)
        }
        submissionRepository.delete(submission)
    }

    override fun getSubmissionFile(submissionId: Long, fileName: String): Pair<ByteArrayInputStream, String> = runBlocking {
        val submission = findSubmissionById(submissionId)
        val file = submission.files.find { it.name == fileName }
            ?: throw ApiException(ErrorCode.NOT_FOUND, "文件不存在")
            
        val (content, contentType) = fileServiceClient.getFile(file.path)
        ByteArrayInputStream(content) to contentType
    }

    private fun findSubmissionById(id: Long): Submission {
        return submissionRepository.findById(id)
            .orElseThrow { ApiException(ErrorCode.NOT_FOUND, "提交记录不存在") }
    }

    private fun Submission.toResponse() = SubmissionResponse(
        id = id!!,
        formData = objectMapper.readValue(formData, formDataTypeRef),
        files = files.map { SubmissionFileInfo(it.name, it.size, it.type) },
        submittedAt = submittedAt,
        updatedAt = updatedAt
    )
} 