package com.lnyynet.filecollect.task.model.dto

import com.lnyynet.filecollect.task.model.entity.DownloadTaskStatus
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class CreateDownloadTaskRequest(
    @field:NotNull(message = "下载类型不能为空")
    val type: DownloadType,
    
    val submissionId: Long? = null,
    
    val settings: DownloadSettings? = null
)

enum class DownloadType {
    ALL, SINGLE
}

data class DownloadSettings(
    val separateArchive: Boolean = false,
    val namePattern: String? = null
)

data class DownloadTaskResponse(
    val id: Long,
    val taskId: Long,
    val type: DownloadType,
    val submissionId: Long?,
    val status: DownloadTaskStatus,
    val url: String?,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) 