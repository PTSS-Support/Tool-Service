package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.domain.enums.SortOrder
import org.ptss.support.domain.models.Comment

interface ICommentRepository {
    suspend fun getAll(toolId: String, cursor: String?, pageSize: Int, sortOrder: SortOrder): PaginationResponse<Comment>
    suspend fun create(comment: Comment): String
    suspend fun update(toolId: String, commentId: String, content: String): Comment?
    suspend fun delete(toolId: String, commentId: String): Comment?
}