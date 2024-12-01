package com.lnyynet.filecollect.file.service

data class FileUploadResult(
    val bucket: String,
    val path: String,
    val contentType: String,
    val size: Long
)

data class FileDownloadResult(
    val content: ByteArray,
    val contentType: String
)

interface FileService {
    fun uploadFile(content: ByteArray, filename: String, contentType: String): FileUploadResult
    fun getFile(path: String): FileDownloadResult
    fun deleteFile(path: String): Boolean
    fun generatePresignedUrl(path: String, expiresIn: Int): String
} 