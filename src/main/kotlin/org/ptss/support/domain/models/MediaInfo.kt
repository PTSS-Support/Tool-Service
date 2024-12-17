package org.ptss.support.domain.models

import org.ptss.support.domain.enums.MediaType
import java.util.UUID

data class MediaInfo(
    val id: String = UUID.randomUUID().toString(),
    val url: String,
    val type: MediaType
)
