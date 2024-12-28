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
import org.ptss.support.api.dtos.requests.media.CreateMediaInfoRequest
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.common.exceptions.ServiceError

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.MULTIPART_FORM_DATA)
interface IMediaInfoController {
    @POST
    @Operation(summary = "Creating media", description = "creating a new media file")
    @APIResponses(
        APIResponse(
            responseCode = "201",
            description = "Media created successfully",
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
    suspend fun createMediaInfo(
        @Parameter(description = "Tool ID", required = true)
        @PathParam("toolId") toolId: String,
        @Parameter(description = "Create media data", required = true)
        request: CreateMediaInfoRequest
    ): MediaInfoResponse
}