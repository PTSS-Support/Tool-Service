package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.requests.comment.CreateCommentRequest
import org.ptss.support.api.dtos.requests.comment.UpdateCommentRequest
import org.ptss.support.api.dtos.responses.comment.CommentResponse
import org.ptss.support.core.mappers.CommentMapper
import org.ptss.support.core.services.CommentService

@ApplicationScoped
class CommentFacade @Inject constructor(
    private val commentService: CommentService
) {
    suspend fun getAllComments(toolId: String): List<CommentResponse> =
        commentService.getAllCommentsAsync(toolId)
            .map(CommentMapper::toResponse)

    suspend fun createComment(toolId: String, request: CreateCommentRequest): CommentResponse {
        val comment = commentService.createCommentAsync(CommentMapper.toCommand(toolId, request))
        return CommentMapper.toResponse(comment)
    }

    suspend fun updateComment(toolId: String, commentId: String, request: UpdateCommentRequest): CommentResponse {
        val updatedComment = commentService.updateCommentAsync(toolId, commentId, request)
        return CommentMapper.toResponse(updatedComment)
    }


    suspend fun deleteComment(toolId: String, commentId: String) {
        commentService.deleteCommentAsync(toolId, commentId)
    }
}
