package org.ptss.support.core.services

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.commands.CreateToolCommand
import org.ptss.support.domain.commands.DeleteToolCommand
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.interfaces.commands.ICommandHandler
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Tool
import org.ptss.support.domain.queries.tool.GetAllToolsQuery
import org.ptss.support.domain.queries.tool.GetToolByIdQuery
import org.ptss.support.infrastructure.handlers.queries.tool.GetAllToolsQueryHandler
import org.ptss.support.infrastructure.util.executeWithExceptionLoggingAsync
import org.slf4j.LoggerFactory

@ApplicationScoped
class ToolService(
    private val getToolByIdHandler: IQueryHandler<GetToolByIdQuery, Tool?>,
    private val getAllToolsHandler: GetAllToolsQueryHandler,
    private val createToolHandler: ICommandHandler<CreateToolCommand, Tool>,
    private val deleteToolHandler: ICommandHandler<DeleteToolCommand, Unit>
) {
    private val logger = LoggerFactory.getLogger(ToolService::class.java)

    suspend fun getToolByIdAsync(toolId: String): Tool? {
        return logger.executeWithExceptionLoggingAsync(
            operation = {
                getToolByIdHandler.handleAsync(GetToolByIdQuery(toolId))
                    ?: throw APIException(
                        errorCode = ErrorCode.TOOL_NOT_FOUND,
                        message = "Product with ID $toolId not found"
                    )
            },
            logMessage = "Error retrieving product $toolId",
            exceptionHandling = { ex ->
                when (ex) {
                    is APIException -> ex
                    else -> APIException(
                        errorCode = ErrorCode.TOOL_CREATION_ERROR,
                        message = "Unable to retrieve tool with ID: $toolId",
                    )
                }
            }
        )
    }

    suspend fun getAllToolsAsync(): List<Tool> {
        return logger.executeWithExceptionLoggingAsync(
            operation = { getAllToolsHandler.handleAsync(GetAllToolsQuery()) },
            logMessage = "Error retrieving all tools",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.TOOL_CREATION_ERROR,
                    message = "Unable to retrieve tools",
                )
            }
        )
    }

    suspend fun createToolAsync(command: CreateToolCommand): Tool {
        validateToolCommand(command)
        return logger.executeWithExceptionLoggingAsync(
            operation = { createToolHandler.handleAsync(command) },
            logMessage = "Error creating tool ${command.name}",
            exceptionHandling = { ex ->
                APIException(
                    errorCode = ErrorCode.TOOL_CREATION_ERROR,
                    message = "Failed to create tool ${command.name}",
                )
            }
        )
    }

    suspend fun deleteToolAsync(toolId: String) {
        logger.executeWithExceptionLoggingAsync(
            operation = {
                getToolByIdAsync(toolId)
                    ?: throw APIException(
                        errorCode = ErrorCode.TOOL_NOT_FOUND,
                        message = "Tool with ID $toolId not found"
                    )

                deleteToolHandler.handleAsync(DeleteToolCommand(toolId))
            },
            logMessage = "Error deleting tool $toolId",
            exceptionHandling = { ex ->
                when (ex) {
                    is APIException -> ex
                    else -> APIException(
                        errorCode = ErrorCode.TOOL_DELETION_ERROR,
                        message = "Unable to delete tool with ID: $toolId",
                    )
                }
            }
        )
    }

    private fun validateToolCommand(command: CreateToolCommand) {
        require(command.name.isNotBlank()) { "Tool name cannot be empty" }
        require(command.description.isNotBlank()) { "Tool description cannot be empty" }
        require(command.category.isNotEmpty()) { "Tool must have at least one category" }
    }
}