package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Column
import jakarta.persistence.ManyToOne
import jakarta.persistence.JoinColumn
import java.time.Instant
import java.util.UUID
import org.ptss.support.domain.models.Comment

@Entity
@Table(name = "comments")
class CommentEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    var id: UUID? = null

    @ManyToOne(optional = false) // Establishing a foreign key relationship
    @JoinColumn(name = "tool_id", referencedColumnName = "id", nullable = false) // Foreign key column
    lateinit var tool: ToolEntity

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String = ""

    @Column(name = "sender_id", nullable = false)
    var senderId: String = ""

    @Column(name = "sender_name", nullable = false, columnDefinition = "TEXT")
    var senderName: String = ""

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now()

    @Column(name = "last_edited_at")
    var lastEditedAt: Instant? = null

    // Required no-arg constructor for JPA
    constructor()

    // Secondary constructor for creating entity with all fields
    constructor(
        id: UUID?,
        tool: ToolEntity,
        content: String,
        senderId: String,
        senderName: String,
        createdAt: Instant = Instant.now(),
        lastEditedAt: Instant? = null
    ) {
        this.id = id
        this.tool = tool
        this.content = content
        this.senderId = senderId
        this.senderName = senderName
        this.createdAt = createdAt
        this.lastEditedAt = lastEditedAt
    }

    fun toDomain() = Comment(
        id = id.toString(),
        toolId = tool.id.toString(), // Reference the ID of the tool entity
        content = content,
        senderId = senderId,
        senderName = senderName,
        createdAt = createdAt,
        lastEditedAt = lastEditedAt
    )

    companion object {
        fun fromDomain(comment: Comment, toolEntity: ToolEntity) = CommentEntity(
            id = if (comment.id.isNotBlank()) UUID.fromString(comment.id) else null, // Ensure null when not set
            tool = toolEntity,
            content = comment.content,
            senderId = comment.senderId,
            senderName = comment.senderName,
            createdAt = comment.createdAt,
            lastEditedAt = comment.lastEditedAt
        )
    }
}
