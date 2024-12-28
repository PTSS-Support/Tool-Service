package org.ptss.support.domain.models

import java.util.UUID

data class MediaInfo(
    val id: String = UUID.randomUUID().toString(),
    val toolId: String,
    val url: String,
    val href: String?
)
