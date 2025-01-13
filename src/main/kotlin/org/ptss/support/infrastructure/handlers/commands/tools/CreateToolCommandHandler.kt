package org.ptss.support.infrastructure.handlers.commands.tools

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.tools.CreateToolCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.repositories.ToolRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.ptss.support.security.context.AuthenticatedUserContext
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.*

@ApplicationScoped
class CreateToolCommandHandler(
    private val toolRepository: ToolRepository,
    private val authenticatedUserContext: AuthenticatedUserContext
) : ICommandHandler<CreateToolCommand, Tool> {

    private val logger = LoggerFactory.getLogger(CreateToolCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateToolCommand): Tool {
        val contextElement = authenticatedUserContext.asCoroutineContext()

        return withContext(Dispatchers.IO + contextElement) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    val userContext = authenticatedUserContext.getCurrentUser()
                    val tool = Tool(
                        id = UUID.randomUUID().toString(),
                        userId = userContext.userId.toString(),
                        name = command.name,
                        description = command.description,
                        category = command.category,
                        createdBy = userContext.firstName.takeIf { it.isNotBlank() },
                        createdAt = Instant.now()
                    )

                    val createdId = toolRepository.create(tool)
                    toolRepository.getById(createdId) ?: throw APIException(
                        errorCode = ErrorCode.TOOL_CREATION_ERROR,
                        message = "Tool created but couldn't be retrieved"
                    )
                },
                logMessage = "Error creating tool with name: ${command.name}"
            )
        }
    }
}