package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.requests.tools.CreateToolRequest
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.domain.commands.tools.CreateToolCommand
import org.ptss.support.api.dtos.responses.tools.ToolResponse
import org.ptss.support.domain.models.Tool

object ToolMapper {
    fun toResponse(tool: Tool) = ToolResponse(
        id = tool.id,
        name = tool.name,
        description = tool.description,
        createdBy = tool.createdBy,
        createdAt = tool.createdAt,
        media = tool.media.map { mediaInfo ->
            MediaInfoResponse(
                id = mediaInfo.id,
                url = mediaInfo.url,
                type = mediaInfo.type
            )
        }
    )

    fun toCommand(request: CreateToolRequest) = CreateToolCommand(
        name = request.name,
        description = request.description,
        category = request.category,
        createdBy = "Authenticated User" // Replace with actual user from session
    )
}