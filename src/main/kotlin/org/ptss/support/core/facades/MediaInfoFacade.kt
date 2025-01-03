package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.requests.media.CreateMediaInfoRequest
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.core.mappers.MediaInfoMapper
import org.ptss.support.core.services.MediaInfoService

@ApplicationScoped
class MediaInfoFacade @Inject constructor(
    private val mediaInfoService: MediaInfoService
) {
    suspend fun createMediaInfo(toolId: String, request: CreateMediaInfoRequest): MediaInfoResponse {
        val media = mediaInfoService.createMediaInfoAsync(MediaInfoMapper.toCommand(toolId, request))
        return MediaInfoMapper.toResponse(media)
    }

    suspend fun deleteMediaInfo(toolId: String, mediaId: String) =
        mediaInfoService.deleteMediaInfoAsync(toolId, mediaId)
}