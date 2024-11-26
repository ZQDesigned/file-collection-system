package com.lnyynet.filecollect.task.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "submissions")
data class Submission(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    var task: Task,
    
    @Column(nullable = false, columnDefinition = "jsonb")
    var formData: String, // JSON string of form data
    
    @ElementCollection
    @CollectionTable(name = "submission_files")
    var files: Set<SubmissionFile>,
    
    @Column(nullable = false)
    var submittedAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(null, Task(), "{}", emptySet())
}

@Embeddable
data class SubmissionFile(
    @Column(nullable = false)
    var name: String,
    
    @Column(nullable = false)
    var size: Long,
    
    @Column(nullable = false)
    var type: String,
    
    @Column(nullable = false)
    var path: String
) {
    constructor() : this("", 0, "", "")
} 