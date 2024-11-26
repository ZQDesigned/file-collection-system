package com.lnyynet.filecollect.common.model

data class Response<T>(
    val code: Int = 0,
    val message: String = "success",
    val data: T? = null
) {
    companion object {
        fun <T> success(data: T? = null, message: String = "success") = Response(0, message, data)
        
        fun error(code: Int, message: String) = Response<Nothing>(code, message)
    }
} 