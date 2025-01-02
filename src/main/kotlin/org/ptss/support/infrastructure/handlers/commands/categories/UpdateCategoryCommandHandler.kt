package org.ptss.support.infrastructure.handlers.commands.categories

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.categories.UpdateCategoryCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Category
import org.ptss.support.infrastructure.repositories.CategoryRepository

@ApplicationScoped
class UpdateCategoryCommandHandler(
    private val categoryRepository: CategoryRepository
) : ICommandHandler<UpdateCategoryCommand, Category> {

    override suspend fun handleAsync(command: UpdateCategoryCommand): Category {
        return withContext(Dispatchers.IO) {
            categoryRepository.update(command.oldCategory, command.newCategory)
                ?: throw APIException(
                    errorCode = ErrorCode.CATEGORY_NOT_FOUND,
                    message = "Category with name ${command.oldCategory} not found for category"
                )
        }
    }
}
