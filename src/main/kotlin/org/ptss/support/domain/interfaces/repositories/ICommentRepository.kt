package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Comment

interface ICommentRepository {
    fun getAll(toolId: String): List<Comment>
    fun create(comment: Comment): String
    fun update(toolId: String, commentId: String, content: String): Comment?
    fun delete(toolId: String, commentId: String): Comment?
}