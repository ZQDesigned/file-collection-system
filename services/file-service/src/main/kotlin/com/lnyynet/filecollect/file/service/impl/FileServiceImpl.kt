package com.lnyynet.filecollect.file.service.impl

import com.google.protobuf.kotlin.toByteString
import com.lnyynet.filecollect.common.exception.ApiException
import com.lnyynet.filecollect.common.exception.ErrorCode
import com.lnyynet.filecollect.file.grpc.*
import io.minio.*
import io.minio.http.Method
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Value
import java.io.ByteArrayInputStream
import java.util.concurrent.TimeUnit

@GrpcService
class FileServiceImpl(
    private val minioClient: MinioClient,
    @Value("\${minio.bucket}") private val bucket: String
) : FileServiceGrpcKt.FileServiceCoroutineImplBase() {

    override suspend fun uploadFile(request: UploadFileRequest): FileInfo {
        try {
            // 确保 bucket 存在
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build())
            }
            
            // 上传文件
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(request.path)
                    .stream(ByteArrayInputStream(request.content.toByteArray()), request.content.size().toLong(), -1)
                    .contentType(request.contentType)
                    .build()
            )
            
            return FileInfo.newBuilder()
                .setId(request.path)
                .setPath(request.path)
                .setUrl(generatePresignedUrl(request.path, 3600))
                .build()
                
        } catch (e: Exception) {
            throw ApiException(ErrorCode.INTERNAL_ERROR, "文件上传失败: ${e.message}")
        }
    }

    override suspend fun getFile(request: GetFileRequest): FileContent {
        try {
            val obj = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(request.path)
                    .build()
            )
            
            return FileContent.newBuilder()
                .setContent(obj.readAllBytes().toByteString())
                .setContentType(obj.headers()["Content-Type"] ?: "application/octet-stream")
                .build()
                
        } catch (e: Exception) {
            throw ApiException(ErrorCode.NOT_FOUND, "文件不存在")
        }
    }

    override suspend fun deleteFile(request: DeleteFileRequest): DeleteFileResponse {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .`object`(request.path)
                    .build()
            )
            return DeleteFileResponse.newBuilder()
                .setSuccess(true)
                .build()
                
        } catch (e: Exception) {
            throw ApiException(ErrorCode.INTERNAL_ERROR, "文件删除失败: ${e.message}")
        }
    }

    override suspend fun generatePresignedUrl(request: GenerateUrlRequest): GenerateUrlResponse {
        return GenerateUrlResponse.newBuilder()
            .setUrl(generatePresignedUrl(request.path, request.expiresIn))
            .build()
    }
    
    private fun generatePresignedUrl(path: String, expiresIn: Int): String {
        return try {
            minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucket)
                    .`object`(path)
                    .expiry(expiresIn, TimeUnit.SECONDS)
                    .build()
            )
        } catch (e: Exception) {
            throw ApiException(ErrorCode.INTERNAL_ERROR, "生成预签名URL失败: ${e.message}")
        }
    }
} 