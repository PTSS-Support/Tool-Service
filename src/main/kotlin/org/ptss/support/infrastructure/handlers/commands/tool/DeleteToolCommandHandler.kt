package org.ptss.support.infrastructure.handlers.commands.tool

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.commands.tool.DeleteToolCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.infrastructure.repositories.ToolRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class DeleteToolCommandHandler(
    private val toolRepository: ToolRepository
) : ICommandHandler<DeleteToolCommand, Unit> {
    private val logger = LoggerFactory.getLogger(DeleteToolCommandHandler::class.java)

    override suspend fun handleAsync(command: DeleteToolCommand) {
        logger.executeWithExceptionLoggingAsync(
            operation = {
                toolRepository.delete(command.id)
            },
            logMessage = "Error deleting tool with ID: ${command.id}"
        )
    }
}