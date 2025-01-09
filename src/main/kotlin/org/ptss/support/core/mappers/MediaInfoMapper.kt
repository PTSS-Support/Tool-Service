package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.requests.media.CreateMediaInfoRequest
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.domain.commands.media.CreateMediaInfoCommand
import org.ptss.support.domain.models.MediaInfo

object MediaInfoMapper {
    fun toCommand(toolId: String, request: CreateMediaInfoRequest): CreateMediaInfoCommand {
        return CreateMediaInfoCommand(
            toolId = toolId,
            fileData = request.media,
            href = request.href
        )
    }

    fun toResponse(media: MediaInfo): MediaInfoResponse {
        return MediaInfoResponse(
            id = media.id,
            url = media.url,
            href = media.href
        )
    }
}