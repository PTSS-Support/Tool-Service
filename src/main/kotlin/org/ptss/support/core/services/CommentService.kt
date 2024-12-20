package org.ptss.support.core.services

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.comment.CreateCommentCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Comment
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class CommentService(
    private val createCommentHandler: ICommandHandler<CreateCommentCommand, Comment>
) {
    private val logger = LoggerFactory.getLogger(ToolService::class.java)

    suspend fun createCommentAsync(command: CreateCommentCommand): Comment {
        validateCommentCommand(command)
        return logger.executeWithExceptionLoggingAsync(
            operation = { createCommentHandler.handleAsync(command) },
            logMessage = "Error creating comment for tool ${command.toolId}",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.COMMENT_CREATION_ERROR,
                    message = "Failed to create comment for tool ${command.toolId}",
                )
            }
        )
    }

    private fun validateCommentCommand(command: CreateCommentCommand) {
        val validationErrors = mutableListOf<String>()

        when {
            command.toolId.isBlank() -> validationErrors.add("Tool ID cannot be empty")
            command.content.isBlank() -> validationErrors.add("Comment content cannot be empty")
            command.content.length > 1000 -> validationErrors.add("Comment content cannot exceed 1000 characters")
            command.senderId.isBlank() -> validationErrors.add("Sender ID cannot be empty")
            command.senderName.isBlank() -> validationErrors.add("Sender name cannot be empty")
        }

        if (validationErrors.isNotEmpty()) {
            throw APIException(
                errorCode = ErrorCode.COMMENT_VALIDATION_ERROR,
                message = validationErrors.joinToString("; ")
            )
        }
    }
}