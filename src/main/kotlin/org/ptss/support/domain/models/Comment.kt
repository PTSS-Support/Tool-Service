package org.ptss.support.domain.models

import java.time.Instant
import java.time.ZonedDateTime

data class Comment(
    val id: String,
    val toolId: String,
    val content: String,
    val senderId: String,
    val senderName: String,
    val createdAt: Instant,
    val lastEditedAt: Instant? = null
)
