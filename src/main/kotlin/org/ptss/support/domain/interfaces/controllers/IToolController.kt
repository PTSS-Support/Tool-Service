package org.ptss.support.domain.interfaces.controllers

import jakarta.ws.rs.GET
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.ptss.support.api.dtos.responses.ToolResponse
import org.ptss.support.common.exceptions.ServiceError

interface IToolController {
    @GET
    @Operation(summary = "Get all tools", description = "Retrieves a list of all tools")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of tools successfully retrieved",
            content = [Content(schema = Schema(implementation = Array<ToolResponse>::class))]
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
    suspend fun getAllTools(): List<ToolResponse>
}