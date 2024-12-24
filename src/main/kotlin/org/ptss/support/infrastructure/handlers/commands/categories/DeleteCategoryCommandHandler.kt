package org.ptss.support.infrastructure.handlers.commands.categories

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.categories.DeleteCategoryCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.infrastructure.repositories.CategoryRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class DeleteCategoryCommandHandler(
    private val categoryRepository: CategoryRepository
) : ICommandHandler<DeleteCategoryCommand, Unit> {
    private val logger = LoggerFactory.getLogger(DeleteCategoryCommandHandler::class.java)

    override suspend fun handleAsync(command: DeleteCategoryCommand) {
        withContext(Dispatchers.IO) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    categoryRepository.delete(command.categoryName)
                },
                logMessage = "Error deleting category: ${command.categoryName}"
            )
        }
    }
}