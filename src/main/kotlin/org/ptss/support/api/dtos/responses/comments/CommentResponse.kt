package org.ptss.support.api.dtos.responses.comments

import java.time.Instant

data class CommentResponse(
    val id: String,
    val content: String,
    val senderId: String,
    val senderName: String,
    val createdAt: Instant,
    val lastEditedAt: Instant? = null
)
