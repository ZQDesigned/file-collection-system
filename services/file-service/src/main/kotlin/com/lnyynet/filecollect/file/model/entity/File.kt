package com.lnyynet.filecollect.file.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "files")
data class File(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(nullable = false)
    var name: String,
    
    @Column(nullable = false)
    var path: String,
    
    @Column(nullable = false)
    var size: Long,
    
    @Column(nullable = false)
    var type: String,
    
    @Column(nullable = false)
    var uploadedBy: String,
    
    @Column(nullable = false)
    var uploadedAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var expiresAt: LocalDateTime? = null,
    
    @Column(nullable = false)
    var status: FileStatus = FileStatus.ACTIVE
) {
    constructor() : this(null, "", "", 0, "", "", LocalDateTime.now(), null)
}

enum class FileStatus {
    ACTIVE, DELETED
} 