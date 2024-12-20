package org.ptss.support.infrastructure.handlers.commands.tool

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.tool.CreateToolCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.repositories.ToolRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.UUID

@ApplicationScoped
class CreateToolCommandHandler(
    private val toolRepository: ToolRepository
) : ICommandHandler<CreateToolCommand, Tool> {
    private val logger = LoggerFactory.getLogger(CreateToolCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateToolCommand): Tool {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                val tool = Tool(
                    id = UUID.randomUUID().toString(),
                    name = command.name,
                    description = command.description,
                    category = command.category,
                    createdBy = command.createdBy,
                    createdAt = Instant.now()
                )
                val createdId = toolRepository.create(tool)
                // Fetch the created tool to return complete object
                toolRepository.getById(createdId) ?: throw APIException(
                    errorCode = ErrorCode.TOOL_CREATION_ERROR,
                    message = "Tool created but couldn't be retrieved"
                )
            },
            logMessage = "Error creating tool with name: ${command.name}"
        )
    }
}