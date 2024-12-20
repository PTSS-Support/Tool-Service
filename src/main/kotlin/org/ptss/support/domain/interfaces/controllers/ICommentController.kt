package org.ptss.support.domain.interfaces.controllers

import jakarta.ws.rs.POST
import jakarta.ws.rs.PathParam
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.ptss.support.api.dtos.requests.comment.CreateCommentRequest
import org.ptss.support.api.dtos.responses.comment.CommentResponse
import org.ptss.support.common.exceptions.ServiceError

interface ICommentController {
    @POST
    @Operation(summary = "Create comment", description = "Creates a new comment")
    @APIResponses(
        APIResponse(
            responseCode = "201",
            description = "Comment successfully created",
            content = [Content(schema = Schema(implementation = CommentResponse::class))]
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid parameters"
        ),
        APIResponse(
            responseCode = "401",
            description = "Unauthorized"
        ),
        APIResponse(
            responseCode = "403",
            description = "Forbidden"
        ),
        APIResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun createComment(
        @Parameter(description = "Tool ID", required = true)
        @PathParam("toolId") toolId: String, // Capture toolId from the path
        @Parameter(description = "Comment creation data", required = true)
        request: CreateCommentRequest
    ): CommentResponse
}