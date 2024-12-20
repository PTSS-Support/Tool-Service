package org.ptss.support.infrastructure.handlers.commands.comments

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.comments.CreateCommentCommand
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
            },
            logMessage = "Error creating comment for toolId: ${command.toolId}"
        )
    }
}