package org.ptss.support.infrastructure.repositories

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.repositories.ICommentRepository
import org.ptss.support.domain.models.Comment
import java.sql.Timestamp

@ApplicationScoped
class CommentRepository : BaseRepository<Comment>(), ICommentRepository {
    override fun create(comment: Comment): String {
        return useConnection { conn ->
            val query = """
                INSERT INTO comments (id, tool_id, content, sender_id, sender_name, created_at, last_edited_at)
                VALUES (?::UUID, ?::UUID, ?, ?, ?, ?, ?)
            """.trimIndent()
            conn.prepareStatement(query).use { statement ->
                statement.setString(1, comment.id)
                statement.setString(2, comment.toolId)
                statement.setString(3, comment.content)
                statement.setString(4, comment.senderId)
                statement.setString(5, comment.senderName)
                statement.setTimestamp(6, Timestamp.from(comment.createdAt))
                statement.setTimestamp(7, comment.lastEditedAt?.toInstant()?.let { Timestamp.from(it) })
                statement.executeUpdate()
            }
            comment.id
        }
    }
}