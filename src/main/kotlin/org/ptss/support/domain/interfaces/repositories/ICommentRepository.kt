package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Comment

interface ICommentRepository {
    suspend fun getAll(toolId: String): List<Comment>
    suspend fun create(comment: Comment): String
    suspend fun update(toolId: String, commentId: String, content: String): Comment?
    suspend fun delete(toolId: String, commentId: String): Comment?
}