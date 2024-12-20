package org.ptss.support.domain.commands.comment

data class CreateCommentCommand(
    val toolId: String,
    val content: String,
    val senderId: String,
    val senderName: String
)

