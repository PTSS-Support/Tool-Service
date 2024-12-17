package org.ptss.support.core.services

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Tool
import org.ptss.support.domain.queries.GetAllToolsQuery
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class ToolService(
    private val getAllToolsHandler: IQueryHandler<GetAllToolsQuery, List<Tool>>
) {
    private val logger = LoggerFactory.getLogger(ToolService::class.java)

    suspend fun getAllToolsAsync(): List<Tool> {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getAllToolsHandler.handleAsync(GetAllToolsQuery()) },
            logMessage = "Error retrieving all tools"
        )
    }
}