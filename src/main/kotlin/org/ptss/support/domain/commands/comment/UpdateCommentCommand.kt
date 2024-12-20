package org.ptss.support.domain.commands.comment

data class UpdateCommentCommand(
    val toolId: String,
    val commentId: String,
    val content: String
)
