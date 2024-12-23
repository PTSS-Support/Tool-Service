package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.requests.media.UploadMediaRequest
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.core.mappers.MediaMapper
import org.ptss.support.core.services.MediaService

@ApplicationScoped
class MediaFacade @Inject constructor(
    private val mediaService: MediaService
) {
    suspend fun uploadMedia(toolId: String, request: UploadMediaRequest): MediaInfoResponse {
        val media = mediaService.uploadMediaAsync(MediaMapper.toCommand(toolId, request))
        return MediaMapper.toResponse(media)
    }
}