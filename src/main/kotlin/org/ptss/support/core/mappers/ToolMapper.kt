package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.responses.MediaInfoResponse
import org.ptss.support.api.dtos.responses.ToolResponse
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
}