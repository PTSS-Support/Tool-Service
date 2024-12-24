package org.ptss.support.domain.interfaces.controllers
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation

import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.ptss.support.api.dtos.requests.comments.CreateCommentRequest
import org.ptss.support.api.dtos.requests.comments.UpdateCommentRequest
import org.ptss.support.api.dtos.responses.comments.CommentResponse
import org.ptss.support.common.exceptions.ServiceError

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface ICommentController {
    @GET
    @Operation(summary = "Get all comments", description = "Retrieves a list of all comments")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of comments successfully retrieved",
            content = [Content(schema = Schema(implementation = Array<CommentResponse>::class))]
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
    suspend fun getAllComments(
        @Parameter(description = "Tool ID", required = true)
        @PathParam("toolId") toolId: String
    ): List<CommentResponse>

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

    @PATCH
    @Path("/{commentId}")
    @Operation(summary = "Update a comment", description = "Updates an existing comment by its ID and associated tool ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Comment successfully updated",
            content = [Content(schema = Schema(implementation = CommentResponse::class))]
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid input"
        ),
        APIResponse(
            responseCode = "401",
            description = "Unauthorized"
        ),
        APIResponse(
            responseCode = "403",
            description = "Not authorized to update this comment",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        ),
        APIResponse(
            responseCode = "404",
            description = "Comment or tool not found",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        ),
        APIResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun updateComment(
        @Parameter(description = "Tool ID", required = true) @PathParam("toolId") toolId: String,
        @Parameter(description = "Comment ID", required = true) @PathParam("commentId") commentId: String,
        @Parameter(description = "Comment update data", required = true) request: UpdateCommentRequest
    ): CommentResponse

    @DELETE
    @Path("/{commentId}")
    @Operation(summary = "Delete a comment", description = "Deletes a specific comment by its ID and associated tool ID")
    @APIResponses(
        APIResponse(
            responseCode = "204",
            description = "Comment successfully deleted"
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid input"
        ),
        APIResponse(
            responseCode = "401",
            description = "Unauthorized"
        ),
        APIResponse(
            responseCode = "403",
            description = "Not authorized to delete this comment",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        ),
        APIResponse(
            responseCode = "404",
            description = "Comment or tool not found",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        ),
        APIResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun deleteComment(
        @Parameter(description = "Tool ID", required = true) @PathParam("toolId") toolId: String,
        @Parameter(description = "Comment ID", required = true) @PathParam("commentId") commentId: String
    ): Response
}