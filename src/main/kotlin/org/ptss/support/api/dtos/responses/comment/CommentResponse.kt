package org.ptss.support.api.dtos.responses.comment

import java.time.Instant
import java.time.ZonedDateTime

data class CommentResponse(
    val id: String,
    val content: String,
    val senderId: String,
    val senderName: String,
    val createdAt: Instant,
    val lastEditedAt: ZonedDateTime? = null
)
