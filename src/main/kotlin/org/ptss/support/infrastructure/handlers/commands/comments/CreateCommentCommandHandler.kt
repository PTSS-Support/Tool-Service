package org.ptss.support.infrastructure.handlers.commands.comments

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.comments.CreateCommentCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Comment
import org.ptss.support.infrastructure.repositories.CommentRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.ptss.support.security.context.AuthenticatedUserContext
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

@ApplicationScoped
class CreateCommentCommandHandler(
    private val commentRepository: CommentRepository,
    private val authenticatedUserContext: AuthenticatedUserContext
) : ICommandHandler<CreateCommentCommand, Comment> {
    private val logger = LoggerFactory.getLogger(CreateCommentCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateCommentCommand): Comment {
        val contextElement = authenticatedUserContext.asCoroutineContext()

        return withContext(Dispatchers.IO + contextElement) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    val userContext = authenticatedUserContext.getCurrentUser()
                    val comment = Comment(
                        id = UUID.randomUUID().toString(),
                        toolId = command.toolId,
                        content = command.content,
                        senderId = userContext.userId.toString(),
                        senderName = userContext.firstName,
                        createdAt = Instant.now()
                    )
                    commentRepository.create(comment)
                    comment
                },
                logMessage = "Error creating comment for toolId: ${command.toolId}"
            )
        }
    }
}
