package org.ptss.support.domain.models

import java.time.Instant
import java.util.*

data class Tool(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String,
    val category: List<String>,
    val createdBy: String,
    val createdAt: Instant,
    val media: MediaInfo? = null
)
