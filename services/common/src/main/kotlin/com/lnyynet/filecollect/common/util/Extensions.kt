package com.lnyynet.filecollect.common.util

import com.lnyynet.filecollect.common.model.Response
import org.springframework.http.ResponseEntity

fun <T> T.toResponse(): Response<T> = Response.success(this)

fun <T> Response<T>.toResponseEntity(): ResponseEntity<Response<T>> = ResponseEntity.ok(this) 