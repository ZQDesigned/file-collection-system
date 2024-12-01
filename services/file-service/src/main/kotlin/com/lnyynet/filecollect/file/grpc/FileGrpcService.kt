package com.lnyynet.filecollect.file.grpc

import com.google.protobuf.ByteString
import com.lnyynet.filecollect.file.service.FileService
import net.devh.boot.grpc.server.service.GrpcService
import org.springframework.beans.factory.annotation.Autowired

@GrpcService
class FileGrpcService @Autowired constructor(
    private val fileService: FileService
) : FileServiceGrpcKt.FileServiceCoroutineImplBase() {

    override suspend fun uploadFile(request: UploadFileRequest): FileInfo {
        val result = fileService.uploadFile(
            content = request.content.toByteArray(),
            filename = request.filename,
            contentType = request.contentType
        )
        return FileInfo.newBuilder()
            .setBucket(result.bucket)
            .setPath(result.path)
            .setContentType(result.contentType)
            .setSize(result.size)
            .build()
    }

    override suspend fun getFile(request: GetFileRequest): FileContent {
        val result = fileService.getFile(request.path)
        return FileContent.newBuilder()
            .setContent(ByteString.copyFrom(result.content))
            .setContentType(result.contentType)
            .build()
    }

    override suspend fun deleteFile(request: DeleteFileRequest): DeleteFileResponse {
        val success = fileService.deleteFile(request.path)
        return DeleteFileResponse.newBuilder()
            .setSuccess(success)
            .build()
    }

    override suspend fun generatePresignedUrl(request: GenerateUrlRequest): GenerateUrlResponse {
        val url = fileService.generatePresignedUrl(request.path, request.expiresIn)
        return GenerateUrlResponse.newBuilder()
            .setUrl(url)
            .build()
    }
} 