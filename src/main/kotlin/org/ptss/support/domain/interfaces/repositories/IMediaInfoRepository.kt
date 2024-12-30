package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.MediaInfo

interface IMediaInfoRepository {
    suspend fun create(mediaInfo: MediaInfo): MediaInfo
    suspend fun delete(toolId: String, mediaId: String): MediaInfo?
}