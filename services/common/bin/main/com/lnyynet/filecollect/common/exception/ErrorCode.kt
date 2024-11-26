package com.lnyynet.filecollect.common.exception

object ErrorCode {
    const val UNAUTHORIZED = 401
    const val FORBIDDEN = 403
    const val NOT_FOUND = 404
    const val VALIDATION_ERROR = 422
    const val INTERNAL_ERROR = 500
    
    fun getMessage(code: Int): String = when (code) {
        UNAUTHORIZED -> "未授权访问"
        FORBIDDEN -> "无权限访问"
        NOT_FOUND -> "资源不存在"
        VALIDATION_ERROR -> "请求参数验证失败"
        INTERNAL_ERROR -> "服务器内部错误"
        else -> "未知错误"
    }
} 