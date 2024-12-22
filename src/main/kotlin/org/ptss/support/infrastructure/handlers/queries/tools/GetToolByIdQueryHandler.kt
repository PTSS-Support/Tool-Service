package org.ptss.support.infrastructure.handlers.queries.tools

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Tool
import org.ptss.support.domain.queries.tools.GetToolByIdQuery
import org.ptss.support.infrastructure.repositories.ToolRepository
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class GetToolByIdQueryHandler (
    private val toolRepository: ToolRepository
) : IQueryHandler<GetToolByIdQuery, Tool?> {
    private val logger = LoggerFactory.getLogger(GetToolByIdQueryHandler::class.java)

    override suspend fun handleAsync(query: GetToolByIdQuery): Tool? {
        return withContext(Dispatchers.IO) {
            logger.executeWithExceptionLoggingAsync(
                operation = {
                    toolRepository.getById(query.id)
                },
                logMessage = "Error retrieving product with ID: ${query.id}"
            )
        }
    }
}