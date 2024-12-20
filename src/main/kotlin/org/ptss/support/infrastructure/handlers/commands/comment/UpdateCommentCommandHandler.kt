package org.ptss.support.infrastructure.handlers.commands.comment

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.comment.UpdateCommentCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Comment
import org.ptss.support.infrastructure.repositories.CommentRepository


@ApplicationScoped
class UpdateCommentCommandHandler(
    private val commentRepository: CommentRepository
) : ICommandHandler<UpdateCommentCommand, Comment> {
    override suspend fun handleAsync(command: UpdateCommentCommand): Comment {
        return commentRepository.update(command.toolId, command.commentId, command.content)
            ?: throw APIException(
                errorCode = ErrorCode.COMMENT_NOT_FOUND,
                message = "Comment with ID ${command.commentId} not found for tool ${command.toolId}"
            )
    }
}