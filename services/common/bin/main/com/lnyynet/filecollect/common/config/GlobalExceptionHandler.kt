package com.lnyynet.filecollect.common.config

import com.lnyynet.filecollect.common.exception.ApiException
import com.lnyynet.filecollect.common.exception.ErrorCode
import com.lnyynet.filecollect.common.model.Response
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.MethodArgumentNotValidException

@RestControllerAdvice
class GlobalExceptionHandler {
    
    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<Response<Nothing>> {
        return ResponseEntity.ok(Response.error(ex.errorCode, ex.message))
    }
    
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseEntity<Response<Nothing>> {
        val message = ex.bindingResult.fieldErrors
            .joinToString("; ") { "${it.field}: ${it.defaultMessage}" }
        return ResponseEntity.ok(Response.error(ErrorCode.VALIDATION_ERROR, message))
    }
    
    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<Response<Nothing>> {
        return ResponseEntity.ok(Response.error(ErrorCode.INTERNAL_ERROR, "Internal Server Error"))
    }
} 