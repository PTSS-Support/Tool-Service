package org.ptss.support.domain.interfaces.controllers

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.ptss.support.api.dtos.requests.comments.CreateCommentRequest
import org.ptss.support.api.dtos.requests.media.UploadMediaRequest
import org.ptss.support.api.dtos.responses.comments.CommentResponse
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.common.exceptions.ServiceError

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
interface IMediaController {
    @POST
    @Operation(summary = "Uploading media", description = "uploading a new media file")
    @APIResponses(
        APIResponse(
            responseCode = "201",
            description = "Media uploaded successfully",
            content = [Content(schema = Schema(implementation = MediaInfoResponse::class))]
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid input or file type or size"
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
    suspend fun uploadMedia(
        @Parameter(description = "Tool ID", required = true)
        @PathParam("toolId") toolId: String,
        @Parameter(description = "Upload media data", required = true)
        request: UploadMediaRequest
    ): MediaInfoResponse
}