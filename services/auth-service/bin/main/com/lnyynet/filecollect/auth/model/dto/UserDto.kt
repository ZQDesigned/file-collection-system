package com.lnyynet.filecollect.auth.model.dto

import com.lnyynet.filecollect.auth.model.entity.UserRole
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserRequest(
    @field:NotBlank(message = "用户名不能为空")
    @field:Size(min = 4, max = 20, message = "用户名长度必须在4-20之间")
    val username: String,
    
    @field:NotBlank(message = "密码不能为空")
    @field:Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    val password: String,
    
    val role: UserRole = UserRole.USER
)

data class UserResponse(
    val id: Long,
    val username: String,
    val role: UserRole
) 