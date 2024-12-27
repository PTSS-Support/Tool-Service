package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.requests.tools.CreateToolRequest
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.api.dtos.responses.tools.ToolResponse
import org.ptss.support.core.mappers.PaginationMapper
import org.ptss.support.core.mappers.ToolMapper
import org.ptss.support.core.services.ToolService

@ApplicationScoped
class ToolFacade @Inject constructor(
    private val toolService: ToolService
) {
    suspend fun getAllTools(cursor: String?, pageSize: Int, sortOrder: String): PaginationResponse<ToolResponse> {
        val result = toolService.getAllToolsAsync(cursor, pageSize, sortOrder)
        return PaginationMapper.mapPaginationResponse(result, ToolMapper::toResponse)
    }


    suspend fun getToolById(id: String): ToolResponse? =
        toolService.getToolByIdAsync(id)
            ?.let(ToolMapper::toResponse)

    suspend fun createTool(request: CreateToolRequest): ToolResponse {
        val tool = toolService.createToolAsync(ToolMapper.toCommand(request))
        return ToolMapper.toResponse(tool)
    }

    suspend fun deleteTool(id: String) {
        toolService.deleteToolAsync(id)
    }
}