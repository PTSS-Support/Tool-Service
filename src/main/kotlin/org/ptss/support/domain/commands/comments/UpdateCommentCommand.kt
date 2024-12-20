package org.ptss.support.domain.commands.comments

data class UpdateCommentCommand(
    val toolId: String,
    val commentId: String,
    val content: String
)
