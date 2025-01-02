package org.ptss.support.infrastructure.handlers.commands.categories

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.categories.CreateCategoryCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Category
import org.ptss.support.infrastructure.repositories.CategoryRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory
import java.time.Instant
import java.util.UUID

@ApplicationScoped
class CreateCategoryCommandHandler(
    private val categoryRepository: CategoryRepository
) : ICommandHandler<CreateCategoryCommand, Category> {
    private val logger = LoggerFactory.getLogger(CreateCategoryCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateCategoryCommand): Category {
        return withContext(Dispatchers.IO) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    val category = Category(
                        category = command.category,
                        groupId = UUID.randomUUID().toString(),
                        createdAt = Instant.now(),
                        tools = emptyList()
                    )
                    categoryRepository.create(category)
                    category
                },
                logMessage = "Error creating category with name: ${command.category}"
            )
        }
    }
}