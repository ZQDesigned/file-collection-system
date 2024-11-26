package com.lnyynet.filecollect.notification.repository

import com.lnyynet.filecollect.notification.model.entity.Message
import com.lnyynet.filecollect.notification.model.entity.MessageStatus
import com.lnyynet.filecollect.notification.model.entity.MessageType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MessageRepository : JpaRepository<Message, Long> {
    fun findByUserIdOrderByCreatedAtDesc(userId: String, pageable: Pageable): Page<Message>
    fun findByUserIdAndStatusOrderByCreatedAtDesc(userId: String, status: MessageStatus, pageable: Pageable): Page<Message>
    fun countByUserId(userId: String): Long
    fun countByUserIdAndStatus(userId: String, status: MessageStatus): Long
    
    @Query("""
        SELECT COUNT(m) FROM Message m 
        WHERE m.userId = :userId 
        AND m.status = 'UNREAD' 
        AND m.createdAt >= :since
    """)
    fun countRecentUnread(userId: String, since: java.time.LocalDateTime): Long

    @Query("""
        SELECT m FROM Message m 
        WHERE m.userId = :userId 
        AND (:status IS NULL OR m.status = :status)
        AND (:type IS NULL OR m.type = :type)
        AND (:tags IS NULL OR m.tags IN :tags)
        AND (
            LOWER(m.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(m.content) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
        ORDER BY m.createdAt DESC
    """)
    fun searchMessages(
        userId: String,
        status: MessageStatus?,
        type: MessageType?,
        tags: List<String>?,
        keyword: String,
        pageable: Pageable
    ): Page<Message>
    
    @Query("""
        SELECT m.tags as tag, COUNT(m) as count 
        FROM Message m 
        WHERE m.userId = :userId 
        GROUP BY m.tags
    """)
    fun countMessagesByTag(userId: String): List<TagCount>
    
    interface TagCount {
        fun getTag(): String
        fun getCount(): Long
    }
} 