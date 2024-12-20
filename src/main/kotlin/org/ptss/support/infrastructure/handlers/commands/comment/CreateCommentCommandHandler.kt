package org.ptss.support.infrastructure.handlers.commands.comment

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.responses.comment.CommentResponse
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.comment.CreateCommentCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Comment
import org.ptss.support.infrastructure.repositories.CommentRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.UUID

@ApplicationScoped
class CreateCommentCommandHandler(
    private val commentRepository: CommentRepository
) : ICommandHandler<CreateCommentCommand, Comment> {
    private val logger = LoggerFactory.getLogger(CreateCommentCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateCommentCommand): Comment {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                val comment = Comment(
                    id = UUID.randomUUID().toString(),
                    toolId = command.toolId,
                    content = command.content,
                    senderId = command.senderId,
                    senderName = command.senderName,
                    createdAt = Instant.now()
                )
                commentRepository.create(comment)

                // Return the complete comment object
                comment
                /*val createdId = commentRepository.create(comment)
                // Fetch the created comment to return a complete object
                commentRepository.getById(createdId) ?: throw APIException(
                    errorCode = ErrorCode.COMMENT_CREATION_ERROR,
                    message = "Comment created but couldn't be retrieved"
                )*/
            },
            logMessage = "Error creating comment for toolId: ${command.toolId}"
        )
    }
}