package org.ptss.support.infrastructure.handlers.commands.categories

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.commands.categories.CreateCategoryCommand
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Category
import org.ptss.support.infrastructure.repositories.CategoryRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.ptss.support.security.context.AuthenticatedUserContext
import org.slf4j.LoggerFactory
import java.time.Instant

@ApplicationScoped
class CreateCategoryCommandHandler(
    private val categoryRepository: CategoryRepository,
    private val authenticatedUserContext: AuthenticatedUserContext
) : ICommandHandler<CreateCategoryCommand, Category> {
    private val logger = LoggerFactory.getLogger(CreateCategoryCommandHandler::class.java)

    override suspend fun handleAsync(command: CreateCategoryCommand): Category {
        val contextElement = authenticatedUserContext.asCoroutineContext()

        return withContext(Dispatchers.IO + contextElement) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    val userContext = authenticatedUserContext.getCurrentUser()
                    val category = Category(
                        category = command.category,
                        groupId = userContext.groupId.toString(),
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