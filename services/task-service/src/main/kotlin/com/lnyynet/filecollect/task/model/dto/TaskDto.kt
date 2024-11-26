package com.lnyynet.filecollect.task.model.dto

import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class CreateTaskRequest(
    @field:NotBlank(message = "标题不能为空")
    @field:Size(max = 100, message = "标题长度不能超过100个字符")
    val title: String,
    
    @field:NotBlank(message = "描述不能为空")
    val description: String,
    
    @field:NotNull(message = "截止时间不能为空")
    @field:Future(message = "截止时间必须是将来的时间")
    val deadline: LocalDateTime,
    
    @field:NotEmpty(message = "至少需要指定一种文件类型")
    val fileTypes: Set<String>,
    
    @field:Min(value = 1, message = "最大文件数量必须大于0")
    @field:Max(value = 10, message = "最大文件数量不能超过10")
    val maxFiles: Int,
    
    @field:NotNull(message = "表单字段不能为空")
    val formFields: List<FormField>
)

data class FormField(
    @field:NotBlank(message = "字段标签不能为空")
    val label: String,
    
    @field:NotBlank(message = "字段类型不能为空")
    val type: String,
    
    val required: Boolean = false,
    
    val validation: FormFieldValidation? = null
)

data class FormFieldValidation(
    val pattern: String? = null,
    val message: String? = null
)

data class TaskResponse(
    val id: Long,
    val title: String,
    val description: String,
    val deadline: LocalDateTime,
    val fileTypes: Set<String>,
    val maxFiles: Int,
    val formFields: List<FormField>,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val submissionCount: Int = 0
)

data class TaskDetailResponse(
    val id: Long,
    val title: String,
    val description: String,
    val deadline: LocalDateTime,
    val fileTypes: Set<String>,
    val maxFiles: Int,
    val formFields: List<FormField>,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val submissionCount: Int = 0
) 