package org.ptss.support.infrastructure.repositories

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.repositories.ICommentRepository
import org.ptss.support.domain.models.Comment
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.persistence.entities.CommentEntity
import java.sql.Timestamp
import java.time.Instant

@ApplicationScoped
class CommentRepository : BaseRepository<Comment>(), ICommentRepository {
    override fun getAll(toolId: String): List<Comment> {
        return useConnection { conn ->
            val query = """
                SELECT c.id, c.tool_id, c.content, c.sender_id, c.sender_name, c.created_at, c.last_edited_at 
                FROM comments c
                JOIN tools t ON t.id = c.tool_id
                WHERE c.tool_id = ?::UUID
            """
            conn.prepareStatement(query).use { statement ->
                statement.setString(1, toolId)
                val resultSet = statement.executeQuery()
                generateSequence {
                    if (resultSet.next()) CommentEntity.fromResultSet(resultSet)
                    else null
                }.map { it.toDomain() }.toList()
            }
        }
    }

    override fun update(toolId: String, commentId: String, content: String): Comment? {
        return useConnection { conn ->
            val query = """
            UPDATE comments 
            SET content = ?, last_edited_at = ? 
            WHERE id = ?::UUID AND tool_id = ?::UUID
            RETURNING id, tool_id, content, sender_id, sender_name, created_at, last_edited_at
        """.trimIndent()

            conn.prepareStatement(query).use { statement ->
                statement.setString(1, content)
                statement.setTimestamp(2, Timestamp.from(Instant.now()))
                statement.setString(3, commentId)
                statement.setString(4, toolId)

                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    CommentEntity.fromResultSet(resultSet).toDomain()
                } else null
            }
        }
    }

    override fun delete(toolId: String, commentId: String): Comment? {
        return useConnection { conn ->
            val query = """
            DELETE FROM comments
            WHERE id = ?::UUID AND tool_id = ?::UUID
            RETURNING id, tool_id, content, sender_id, sender_name, created_at, last_edited_at
        """.trimIndent()
            conn.prepareStatement(query).use { statement ->
                statement.setString(1, commentId)
                statement.setString(2, toolId)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) CommentEntity.fromResultSet(resultSet).toDomain() else null
            }
        }
    }


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
                statement.setTimestamp(7, comment.lastEditedAt?.let { Timestamp.from(it) })
                statement.executeUpdate()
            }
            comment.id
        }
    }

}