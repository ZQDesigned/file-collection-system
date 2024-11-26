package com.lnyynet.filecollect.task.model.dto

import java.time.LocalDateTime

data class SubmissionResponse(
    val id: Long,
    val formData: Map<String, String>,
    val files: List<SubmissionFileInfo>,
    val submittedAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

data class SubmissionFileInfo(
    val name: String,
    val size: Long,
    val type: String
)

data class CreateSubmissionRequest(
    val formData: Map<String, String>
) 