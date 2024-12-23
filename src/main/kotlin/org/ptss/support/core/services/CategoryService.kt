package org.ptss.support.core.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.models.Category
import org.ptss.support.domain.models.Comment
import org.ptss.support.domain.queries.categories.GetAllCategoriesQuery
import org.ptss.support.domain.queries.comments.GetAllCommentsQuery
import org.ptss.support.infrastructure.handlers.queries.categories.GetAllCategoriesQueryHandler
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class CategoryService @Inject constructor(
    private val getAllCategoriesHandler: GetAllCategoriesQueryHandler,
) {
    private val logger = LoggerFactory.getLogger(CategoryService::class.java)

    suspend fun getAllCategoriesAsync(): List<Category> {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getAllCategoriesHandler.handleAsync(GetAllCategoriesQuery()) },
            logMessage = "Error retrieving all categories",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.CATEGORY_CREATION_ERROR,
                    message = "Unable to retrieve categories",
                )
            }
        )
    }
}