package org.ptss.support.domain.models

import java.time.Instant
import java.time.ZonedDateTime

data class Comment(
    val id: String, // Unique identifier for the comment
    val toolId: String, // ID of the tool the comment is associated with
    val content: String, // The actual comment content
    val senderId: String, // The ID of the user who created the comment
    val senderName: String, // The name of the user who created the comment
    val createdAt: Instant, // Timestamp when the comment was created
    val lastEditedAt: ZonedDateTime? = null // Timestamp when the comment was last edited, if applicable
)
