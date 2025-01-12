package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.requests.tools.CreateToolRequest
import org.ptss.support.api.dtos.responses.tools.ToolResponse
import org.ptss.support.api.dtos.responses.tools.ToolSummaryResponse
import org.ptss.support.domain.commands.tools.CreateToolCommand
import org.ptss.support.domain.models.Tool

object ToolMapper {
    fun toSummaryResponse(tool: Tool) = ToolSummaryResponse(
        id = tool.id,
        name = tool.name,
        createdBy = tool.createdBy
    )

    fun toResponse(tool: Tool) = ToolResponse(
        id = tool.id,
        name = tool.name,
        description = tool.description,
        createdBy = tool.createdBy,
        createdAt = tool.createdAt,
        media = tool.media?.let { MediaInfoMapper.toResponse(it) }
    )

    fun toCommand(request: CreateToolRequest) = CreateToolCommand(
        name = request.name,
        description = request.description,
        category = request.category
    )
}