package org.ptss.support.api.dtos.responses

import org.ptss.support.domain.enums.MediaType

data class MediaInfoResponse(
    val id: String,
    val url: String,
    val type: MediaType
)
