package org.ptss.support.infrastructure.handlers.commands.tools

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.tools.DeleteToolCommand
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
        withContext(Dispatchers.IO) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    toolRepository.delete(command.id)
                },
                logMessage = "Error deleting tool with ID: ${command.id}"
            )
        }
    }
}