package org.ptss.support.domain.interfaces.controllers

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.Produces
import jakarta.ws.rs.POST
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.jboss.resteasy.reactive.RestForm
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.common.exceptions.ServiceError
import java.io.InputStream

@Consumes(MediaType.MULTIPART_FORM_DATA)
@Produces(MediaType.APPLICATION_JSON)
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
        @Parameter(description = "Tool ID", required = true) @PathParam("toolId") toolId: String,
        @Parameter(description = "Media file", required = true) @RestForm("media") media: InputStream,
        @Parameter(description = "Optional href", required = false) @RestForm("href") href: String?
    ): MediaInfoResponse

    @DELETE
    @Path("/{mediaId}")
    @Operation(summary = "Delete media file", description = "Deletes a media file by its ID and associated tool ID")
    @APIResponses(
        APIResponse(
            responseCode = "204",
            description = "Media successfully deleted"
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
            description = "Not authorized to delete this media file",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        ),
        APIResponse(
            responseCode = "404",
            description = "Media or tool not found",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        ),
        APIResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun deleteMediaInfo(
        @Parameter(description = "Tool ID", required = true) @PathParam("toolId") toolId: String,
        @Parameter(description = "Media ID", required = true) @PathParam("mediaId") mediaId: String
    ): Response
}