package com.lnyynet.filecollect.task.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tasks")
data class Task(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    @Column(nullable = false)
    var title: String,
    
    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String,
    
    @Column(nullable = false)
    var deadline: LocalDateTime,
    
    @ElementCollection
    @CollectionTable(name = "task_file_types")
    var fileTypes: Set<String>,
    
    @Column(nullable = false)
    var maxFiles: Int,
    
    @Column(nullable = false, columnDefinition = "jsonb")
    var formFields: String, // JSON string of form field definitions
    
    @Column(nullable = false)
    var createdBy: String,
    
    @Column(nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) {
    constructor() : this(
        null, "", "", LocalDateTime.now(), 
        emptySet(), 1, "[]", "", 
        LocalDateTime.now(), LocalDateTime.now()
    )
} 