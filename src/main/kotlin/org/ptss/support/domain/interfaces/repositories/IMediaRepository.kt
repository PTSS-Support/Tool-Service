package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Comment
import org.ptss.support.domain.models.MediaInfo

interface IMediaRepository {
    suspend fun upload(media: MediaInfo): MediaInfo
}