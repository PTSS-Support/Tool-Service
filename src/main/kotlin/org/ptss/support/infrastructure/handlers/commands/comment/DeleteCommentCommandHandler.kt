package org.ptss.support.infrastructure.handlers.commands.comment

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.comment.DeleteCommentCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.infrastructure.repositories.CommentRepository

@ApplicationScoped
class DeleteCommentCommandHandler(
    private val commentRepository: CommentRepository
) : ICommandHandler<DeleteCommentCommand, Unit> {
    override suspend fun handleAsync(command: DeleteCommentCommand) {
        commentRepository.delete(command.toolId, command.id)
            ?: throw APIException(
                errorCode = ErrorCode.COMMENT_NOT_FOUND,
                message = "Comment with ID ${command.id} not found for tool ${command.toolId}"
            )
    }
}
