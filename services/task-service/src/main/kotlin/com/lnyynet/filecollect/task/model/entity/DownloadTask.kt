package com.lnyynet.filecollect.task.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "download_tasks")
data class DownloadTask(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    var task: Task,
    
    @Column(nullable = true)
    var submissionId: Long? = null,
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: DownloadTaskStatus = DownloadTaskStatus.PENDING,
    
    @Column(nullable = true)
    var url: String? = null,
    
    @Column(nullable = false)
    var createdBy: String,
    
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(null, Task(), null, DownloadTaskStatus.PENDING, null, "")
}

enum class DownloadTaskStatus {
    PENDING, PROCESSING, COMPLETED, FAILED
} 