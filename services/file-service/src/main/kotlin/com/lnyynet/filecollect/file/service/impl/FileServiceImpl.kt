package com.lnyynet.filecollect.file.service.impl

import com.lnyynet.filecollect.file.service.FileDownloadResult
import com.lnyynet.filecollect.file.service.FileService
import com.lnyynet.filecollect.file.service.FileUploadResult
import io.minio.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.util.concurrent.TimeUnit

@Service
class FileServiceImpl(
    private val minioClient: MinioClient,
    @Value("\${minio.bucket}") private val bucket: String
) : FileService {

    override fun uploadFile(content: ByteArray, filename: String, contentType: String): FileUploadResult {
        val inputStream = ByteArrayInputStream(content)
        val objectName = generateObjectName(filename)
        
        minioClient.putObject(
            PutObjectArgs.builder()
                .bucket(bucket)
                .`object`(objectName)
                .stream(inputStream, content.size.toLong(), -1)
                .contentType(contentType)
                .build()
        )

        return FileUploadResult(
            bucket = bucket,
            path = "/${bucket}/${objectName}",
            contentType = contentType,
            size = content.size.toLong()
        )
    }

    override fun getFile(path: String): FileDownloadResult {
        val obj = minioClient.getObject(
            GetObjectArgs.builder()
                .bucket(bucket)
                .`object`(path)
                .build()
        )

        return FileDownloadResult(
            content = obj.readAllBytes(),
            contentType = obj.headers()["Content-Type"] ?: "application/octet-stream"
        )
    }

    override fun deleteFile(path: String): Boolean {
        return try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(path)
                    .build()
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun generatePresignedUrl(path: String, expiresIn: Int): String {
        return minioClient.getPresignedObjectUrl(
            GetPresignedObjectUrlArgs.builder()
                .bucket(bucket)
                .`object`(path)
                .expiry(expiresIn, TimeUnit.SECONDS)
                .build()
        )
    }

    private fun generateObjectName(filename: String): String {
        val timestamp = System.currentTimeMillis()
        val extension = filename.substringAfterLast(".", "")
        return "${timestamp}_$filename"
    }
} 