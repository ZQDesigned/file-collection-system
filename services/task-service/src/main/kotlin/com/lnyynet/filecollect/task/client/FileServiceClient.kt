package com.lnyynet.filecollect.task.client

import com.google.protobuf.ByteString
import com.lnyynet.filecollect.file.grpc.*
import net.devh.boot.grpc.client.inject.GrpcClient
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileServiceClient {
    @GrpcClient("file-service")
    private lateinit var fileStub: FileServiceGrpcKt.FileServiceCoroutineImplBase

    suspend fun uploadFile(file: MultipartFile, path: String): String {
        val request = UploadFileRequest.newBuilder()
            .setContent(ByteString.copyFrom(file.bytes))
            .setFilename(file.originalFilename ?: "unknown")
            .setContentType(file.contentType ?: "application/octet-stream")
            .setPath(path)
            .build()
            
        val response = fileStub.uploadFile(request)
        return response.path
    }

    suspend fun getFile(path: String): Pair<ByteArray, String> {
        val request = GetFileRequest.newBuilder()
            .setPath(path)
            .build()
            
        val response = fileStub.getFile(request)
        return response.content.toByteArray() to response.contentType
    }

    suspend fun deleteFile(path: String): Boolean {
        val request = DeleteFileRequest.newBuilder()
            .setPath(path)
            .build()
            
        val response = fileStub.deleteFile(request)
        return response.success
    }

    suspend fun generatePresignedUrl(path: String, expiresIn: Int = 3600): String {
        val request = GenerateUrlRequest.newBuilder()
            .setPath(path)
            .setExpiresIn(expiresIn)
            .build()
            
        val response = fileStub.generatePresignedUrl(request)
        return response.url
    }
} 