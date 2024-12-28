package org.ptss.support.core.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.categories.CreateCategoryCommand
import org.ptss.support.domain.commands.categories.DeleteCategoryCommand
import org.ptss.support.domain.commands.categories.UpdateCategoryCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.models.Category
import org.ptss.support.domain.queries.categories.GetAllCategoriesQuery
import org.ptss.support.infrastructure.handlers.queries.categories.GetAllCategoriesQueryHandler
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class CategoryService @Inject constructor(
    private val getAllCategoriesHandler: GetAllCategoriesQueryHandler,
    private val createCategoryHandler: ICommandHandler<CreateCategoryCommand, Category>,
    private val updateCategoryHandler: ICommandHandler<UpdateCategoryCommand, Category>,
    private val deleteCategoryHandler: ICommandHandler<DeleteCategoryCommand, Unit>
) {
    private val logger = LoggerFactory.getLogger(CategoryService::class.java)

    suspend fun getAllCategoriesAsync(): List<Category> {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getAllCategoriesHandler.handleAsync(GetAllCategoriesQuery()) },
            logMessage = "Error retrieving all categories",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.CATEGORY_NOT_FOUND,
                    message = "Unable to retrieve categories",
                )
            }
        )
    }

    suspend fun createCategoryAsync(command: CreateCategoryCommand): Category {
        validateCategoryCommand(command)
        return logger.executeWithExceptionLoggingAsync(
            operation = { createCategoryHandler.handleAsync(command) },
            logMessage = "Error creating category ${command.category}",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.CATEGORY_CREATION_ERROR,
                    message = "Failed to create category ${command.category}",
                )
            }
        )
    }

    suspend fun deleteCategoryAsync(categoryName: String) {
        logger.executeWithExceptionLoggingAsync(
            operation = {
                getAllCategoriesAsync().find { it.category == categoryName }
                    ?: throw APIException(
                        errorCode = ErrorCode.CATEGORY_NOT_FOUND,
                        message = "Category $categoryName not found"
                    )

                deleteCategoryHandler.handleAsync(DeleteCategoryCommand(categoryName))
            },
            logMessage = "Error deleting category $categoryName",
            exceptionHandling = { ex ->
                when (ex) {
                    is APIException -> ex
                    else -> APIException(
                        errorCode = ErrorCode.CATEGORY_DELETION_ERROR,
                        message = "Unable to delete category: $categoryName",
                    )
                }
            }
        )
    }

    suspend fun updateCategoryAsync(oldCategory: String, command: UpdateCategoryCommand): Category {
        validateUpdateCategoryCommand(command)

        return logger.executeWithExceptionLoggingAsync(
            operation = { updateCategoryHandler.handleAsync(command) },
            logMessage = "Error updating category $oldCategory to ${command.newCategory}",
            exceptionHandling = { ex ->
                when (ex) {
                    is APIException -> ex
                    else -> APIException(
                        errorCode = ErrorCode.CATEGORY_UPDATE_ERROR,
                        message = "Failed to update category $oldCategory",
                    )
                }
            }
        )
    }

    private fun validateCategoryCommand(command: CreateCategoryCommand) {
        require(command.category.isNotBlank()) { "Category name cannot be empty" }
    }

    private fun validateUpdateCategoryCommand(command: UpdateCategoryCommand) {
        require(command.newCategory.isNotBlank()) { "New category name cannot be empty" }
        require(command.oldCategory != command.newCategory) { "New category name must be different from the current name" }
    }
}