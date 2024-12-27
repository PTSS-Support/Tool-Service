package org.ptss.support.domain.interfaces.controllers

import com.azure.core.annotation.PathParam
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.DefaultValue
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.ptss.support.api.dtos.requests.tools.CreateToolRequest
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.api.dtos.responses.tools.ToolResponse
import org.ptss.support.common.exceptions.ServiceError

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface IToolController {
    @GET
    @Operation(summary = "Get paginated list of tools")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Successfully retrieved tools",
            content = [Content(schema = Schema(implementation = PaginationResponse::class))]
        ),
        APIResponse(
            responseCode = "400",
            description = "Invalid parameters",
            content = [Content(schema = Schema(implementation = PaginationResponse::class))]
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
    suspend fun getAllTools(
        @Parameter(description = "Cursor for pagination")
        @QueryParam("cursor") cursor: String?,

        @Parameter(description = "Number of items per page (1-50)")
        @QueryParam("pageSize") @DefaultValue("20") pageSize: Int,

        @Parameter(description = "Sort order by creation time (asc/desc)")
        @QueryParam("sortOrder") @DefaultValue("desc") sortOrder: String
    ): PaginationResponse<ToolResponse>

    @GET
    @Path("/{id}")
    @Operation(summary = "Get tool by ID", description = "Retrieves a specific tool by its ID")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Tool successfully retrieved",
            content = [Content(schema = Schema(implementation = ToolResponse::class))]
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
            responseCode = "404",
            description = "Tool not found",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun getToolById(
        @Parameter(description = "ID of the tool", required = true)
        @PathParam("id") id: String
    ): ToolResponse?

    @POST
    @Operation(summary = "Create tool", description = "Creates a new tool")
    @APIResponses(
        APIResponse(
            responseCode = "201",
            description = "Tool successfully created",
            content = [Content(schema = Schema(implementation = ToolResponse::class))]
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
    suspend fun createTool(request: CreateToolRequest): ToolResponse

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete tool by ID", description = "Deletes a specific tool by its ID")
    @APIResponses(
        APIResponse(
            responseCode = "204",
            description = "Tool successfully deleted"
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
            responseCode = "404",
            description = "Tool not found",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        ),
        APIResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun deleteTool(
        @Parameter(description = "ID of the tool to delete", required = true)
        @PathParam("id") id: String
    ): Response
}