package com.lnyynet.filecollect.task.service.impl

import com.lnyynet.filecollect.task.client.FileServiceClient
import com.lnyynet.filecollect.task.model.dto.SubmissionResponse
import com.lnyynet.filecollect.task.model.entity.DownloadTaskStatus
import com.lnyynet.filecollect.task.repository.DownloadTaskRepository
import com.lnyynet.filecollect.task.service.SubmissionService
import kotlinx.coroutines.runBlocking
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Component
class DownloadTaskProcessor(
    private val downloadTaskRepository: DownloadTaskRepository,
    private val submissionService: SubmissionService,
    private val fileServiceClient: FileServiceClient
) {
    
    @RabbitListener(queues = ["download-tasks.queue"])
    @Transactional
    fun processDownloadTask(message: DownloadTaskMessage) = runBlocking {
        val downloadTask = downloadTaskRepository.findById(message.downloadTaskId).get()
        
        try {
            downloadTask.status = DownloadTaskStatus.PROCESSING
            downloadTaskRepository.save(downloadTask)
            
            val zipBytes = ByteArrayOutputStream().use { baos ->
                ZipOutputStream(baos).use { zos ->
                    if (downloadTask.submissionId != null) {
                        processSubmission(downloadTask.submissionId!!, zos, message.settings?.namePattern)
                    } else {
                        val submissions = submissionService.listSubmissions(
                            downloadTask.task.id!!,
                            PageRequest.of(0, 1000)
                        )
                        
                        submissions.forEach { submission ->
                            if (message.settings?.separateArchive == true) {
                                processSubmission(submission.id, zos, message.settings.namePattern)
                            } else {
                                processSubmission(submission.id, zos, message.settings?.namePattern)
                            }
                        }
                    }
                }
                baos.toByteArray()
            }
            
            // 上传到文件服务
            val path = "downloads/${downloadTask.id}.zip"
            val multipartFile = ByteArrayMultipartFile(
                "archive.zip",
                "archive.zip",
                "application/zip",
                zipBytes
            )
            downloadTask.url = fileServiceClient.uploadFile(multipartFile, path)
            downloadTask.status = DownloadTaskStatus.COMPLETED
            downloadTaskRepository.save(downloadTask)
            
        } catch (e: Exception) {
            downloadTask.status = DownloadTaskStatus.FAILED
            downloadTaskRepository.save(downloadTask)
            throw e
        }
    }
    
    private fun processSubmission(submissionId: Long, zos: ZipOutputStream, namePattern: String?) {
        val submission = submissionService.getSubmission(submissionId)
        submission.files.forEach { file ->
            val (inputStream, _) = submissionService.getSubmissionFile(submissionId, file.name)
            zos.putNextEntry(ZipEntry(generateFileName(submission, file.name, namePattern)))
            inputStream.copyTo(zos)
            zos.closeEntry()
        }
    }
    
    private fun generateFileName(submission: SubmissionResponse, originalName: String, pattern: String?): String {
        if (pattern == null) return originalName
        
        var fileName = pattern
        submission.formData.forEach { (key, value) ->
            fileName = fileName!!.replace("{$key}", value)
        }
        
        val extension = originalName.substringAfterLast(".", "")
        return if (extension.isNotEmpty()) "$fileName.$extension" else fileName!!
    }
}

class ByteArrayMultipartFile(
    private val name: String,
    private val originalFilename: String,
    private val contentType: String,
    private val content: ByteArray
) : MultipartFile {
    override fun getName(): String = name
    override fun getOriginalFilename(): String = originalFilename
    override fun getContentType(): String = contentType
    override fun isEmpty(): Boolean = content.isEmpty()
    override fun getSize(): Long = content.size.toLong()
    override fun getBytes(): ByteArray = content
    override fun getInputStream(): InputStream = ByteArrayInputStream(content)
    override fun transferTo(dest: java.io.File) {
        dest.writeBytes(content)
    }
} 