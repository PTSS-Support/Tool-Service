package org.ptss.support.infrastructure.persistence.entities

import org.ptss.support.domain.models.Comment
import java.sql.ResultSet
import java.time.Instant

data class CommentEntity(
    val id: String,
    val toolId: String,
    val content: String,
    val senderId: String,
    val senderName: String,
    val createdAt: Instant,
    val lastEditedAt: Instant?
) {
    fun toDomain(): Comment {
        return Comment(
            id = id,
            toolId = toolId,
            content = content,
            senderId = senderId,
            senderName = senderName,
            createdAt = createdAt,
            lastEditedAt = lastEditedAt
        )
    }

    companion object {
        fun fromResultSet(resultSet: ResultSet): CommentEntity {
            return CommentEntity(
                id = resultSet.getString("id"),
                toolId = resultSet.getString("tool_id"),
                content = resultSet.getString("content"),
                senderId = resultSet.getString("sender_id"),
                senderName = resultSet.getString("sender_name"),
                createdAt = resultSet.getTimestamp("created_at").toInstant(),
                lastEditedAt = resultSet.getTimestamp("last_edited_at")?.toInstant()
            )
        }
    }
}
