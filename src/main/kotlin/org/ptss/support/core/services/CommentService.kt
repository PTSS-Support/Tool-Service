package org.ptss.support.core.services

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.api.dtos.requests.comments.UpdateCommentRequest
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.core.util.ValidatePagination.validatePagination
import org.ptss.support.domain.commands.comments.CreateCommentCommand
import org.ptss.support.domain.commands.comments.DeleteCommentCommand
import org.ptss.support.domain.commands.comments.UpdateCommentCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.enums.SortOrder
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Comment
import org.ptss.support.domain.queries.comments.GetAllCommentsQuery
import org.ptss.support.infrastructure.handlers.queries.comments.GetAllCommentsQueryHandler
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class CommentService(
    private val getAllCommentsHandler: GetAllCommentsQueryHandler,
    private val createCommentHandler: ICommandHandler<CreateCommentCommand, Comment>,
    private val deleteCommentHandler: ICommandHandler<DeleteCommentCommand, Unit>,
    private val updateCommentHandler: ICommandHandler<UpdateCommentCommand, Comment>
) {
    private val logger = LoggerFactory.getLogger(ToolService::class.java)

    suspend fun getAllCommentsAsync(toolId: String, cursor: String?, pageSize: Int, sortOrder: SortOrder): PaginationResponse<Comment> {
        validatePagination(pageSize)

        return logger.executeWithExceptionLoggingAsync(
            operation = {
                getAllCommentsHandler.handleAsync(GetAllCommentsQuery(toolId, cursor, pageSize, sortOrder))
            },
            logMessage = "Error retrieving comments for tool $toolId",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.TOOL_NOT_FOUND,
                    message = "Unable to retrieve comments for tool $toolId",
                )
            }
        )
    }

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

    suspend fun updateCommentAsync(toolId: String, commentId: String, request: UpdateCommentRequest): Comment {
        val command = UpdateCommentCommand(toolId, commentId, request.content)
        validateUpdateCommentCommand(command)

        return logger.executeWithExceptionLoggingAsync(
            operation = { updateCommentHandler.handleAsync(command) },
            logMessage = "Error updating comment $commentId for tool $toolId",
            exceptionHandling = { ex ->
                when (ex) {
                    is APIException -> ex
                    else -> APIException(
                        errorCode = ErrorCode.COMMENT_UPDATE_ERROR,
                        message = "Failed to update comment $commentId for tool $toolId",
                    )
                }
            }
        )
    }

    suspend fun deleteCommentAsync(toolId: String, commentId: String) {
        logger.executeWithExceptionLoggingAsync(
            operation = {
                deleteCommentHandler.handleAsync(DeleteCommentCommand(toolId, commentId))
            },
            logMessage = "Error deleting comment $commentId for tool $toolId",
            exceptionHandling = { ex ->
                when (ex) {
                    is APIException -> ex
                    else -> APIException(
                        errorCode = ErrorCode.COMMENT_DELETION_ERROR,
                        message = "Unable to delete comment with ID $commentId for tool $toolId",
                    )
                }
            }
        )
    }

    private fun validateCommentCommand(command: CreateCommentCommand) {
        val validationErrors = mutableListOf<String>()

        when {
            command.toolId.isBlank() -> validationErrors.add("Tool ID cannot be empty")
            command.content.isBlank() -> validationErrors.add("Comment content cannot be empty")
            command.content.length > 1000 -> validationErrors.add("Comment content cannot exceed 1000 characters")
        }

        if (validationErrors.isNotEmpty()) {
            throw APIException(
                errorCode = ErrorCode.COMMENT_VALIDATION_ERROR,
                message = validationErrors.joinToString("; ")
            )
        }
    }

    private fun validateUpdateCommentCommand(command: UpdateCommentCommand) {
        val validationErrors = mutableListOf<String>()

        when {
            command.toolId.isBlank() -> validationErrors.add("Tool ID cannot be empty")
            command.commentId.isBlank() -> validationErrors.add("Comment ID cannot be empty")
            command.content.isBlank() -> validationErrors.add("Comment content cannot be empty")
            command.content.length > 1000 -> validationErrors.add("Comment content cannot exceed 1000 characters")
        }

        if (validationErrors.isNotEmpty()) {
            throw APIException(
                errorCode = ErrorCode.COMMENT_VALIDATION_ERROR,
                message = validationErrors.joinToString("; ")
            )
        }
    }
}