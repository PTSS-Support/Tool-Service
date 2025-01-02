package org.ptss.support.domain.models

import java.time.Instant
import java.util.UUID

data class Tool(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = UUID.randomUUID().toString(), // Will later be changed to the actual users id
    val name: String,
    val description: String,
    val category: List<String>,
    val createdBy: String,
    val createdAt: Instant,
    val media: List<MediaInfo> = emptyList(),
)
