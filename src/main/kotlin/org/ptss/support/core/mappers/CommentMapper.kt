package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.requests.comments.CreateCommentRequest
import org.ptss.support.api.dtos.responses.comments.CommentResponse
import org.ptss.support.domain.commands.comments.CreateCommentCommand
import org.ptss.support.domain.models.Comment

object CommentMapper {
    fun toCommand(toolId: String, request: CreateCommentRequest) = CreateCommentCommand(
        toolId = toolId,
        content = request.content,
        senderId = "Authenticated User ID", // Replace with actual user ID from session
        senderName = "Authenticated User Name" // Replace with actual user name from session
    )

    fun toResponse(comment: Comment) = CommentResponse(
        id = comment.id,
        content = comment.content,
        senderId = comment.senderId,
        senderName = comment.senderName,
        createdAt = comment.createdAt,
        lastEditedAt = comment.lastEditedAt
    )
}