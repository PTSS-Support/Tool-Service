package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.requests.comments.CreateCommentRequest
import org.ptss.support.api.dtos.requests.comments.UpdateCommentRequest
import org.ptss.support.api.dtos.responses.comments.CommentResponse
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.core.mappers.CommentMapper
import org.ptss.support.core.mappers.PaginationMapper
import org.ptss.support.core.services.CommentService
import org.ptss.support.domain.enums.SortOrder

@ApplicationScoped
class CommentFacade @Inject constructor(
    private val commentService: CommentService
) {
    suspend fun getAllComments(toolId: String, cursor: String?, pageSize: Int, sortOrder: SortOrder
    ): PaginationResponse<CommentResponse> {
        val result = commentService.getAllCommentsAsync(toolId, cursor, pageSize, sortOrder)
        return PaginationMapper.mapPaginationResponse(result, CommentMapper::toResponse)
    }

    suspend fun createComment(toolId: String, request: CreateCommentRequest): CommentResponse {
        val comment = commentService.createCommentAsync(CommentMapper.toCommand(toolId, request))
        return CommentMapper.toResponse(comment)
    }

    suspend fun updateComment(toolId: String, commentId: String, request: UpdateCommentRequest): CommentResponse {
        val updatedComment = commentService.updateCommentAsync(toolId, commentId, request)
        return CommentMapper.toResponse(updatedComment)
    }

    suspend fun deleteComment(toolId: String, commentId: String) =
        commentService.deleteCommentAsync(toolId, commentId)
    
}
