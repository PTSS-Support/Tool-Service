package org.ptss.support.domain.commands.comments

data class CreateCommentCommand(
    val toolId: String,
    val content: String,
    val senderId: String,
    val senderName: String
)

