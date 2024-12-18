package org.ptss.support.domain.models

import java.time.Instant
import java.util.UUID

data class Tool(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    //val media: List<MediaInfo> = emptyList(),
    val createdBy: String,
    val createdAt: Instant
)
