package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.requests.media.UploadMediaRequest
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.domain.commands.media.UploadMediaCommand
import org.ptss.support.domain.models.MediaInfo

object MediaMapper {
    // Mapping from UploadMediaRequest to UploadMediaCommand
    fun toCommand(toolId: String, request: UploadMediaRequest): UploadMediaCommand {
        return UploadMediaCommand(
            toolId = toolId,
            fileData = request.file,
        )
    }

    // Mapping from Media model to MediaInfoResponse
    fun toResponse(media: MediaInfo): MediaInfoResponse {
        return MediaInfoResponse(
            id = media.id,
            url = media.url,
            type = media.type
        )
    }
}