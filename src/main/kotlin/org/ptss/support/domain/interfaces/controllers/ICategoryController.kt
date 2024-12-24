package org.ptss.support.domain.interfaces.controllers

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.Produces
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PATCH
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.ptss.support.api.dtos.requests.categories.CreateCategoryRequest
import org.ptss.support.api.dtos.requests.categories.UpdateCategoryRequest
import org.ptss.support.api.dtos.responses.categories.CategoryResponse
import org.ptss.support.api.dtos.responses.comments.CommentResponse
import org.ptss.support.common.exceptions.ServiceError

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
interface ICategoryController {
    @GET
    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "List of categories successfully retrieved",
            content = [Content(schema = Schema(implementation = Array<CategoryResponse>::class))]
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
    suspend fun getAllCategories(): List<CategoryResponse>

    @PATCH
    @Path("/{categoryName}")
    @Operation(summary = "Update a category name", description = "Updates an existing category by its name")
    @APIResponses(
        APIResponse(
            responseCode = "200",
            description = "Category successfully updated",
            content = [Content(schema = Schema(implementation = CategoryResponse::class))]
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
            description = "Not authorized to update this category",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        ),
        APIResponse(
            responseCode = "404",
            description = "Category not found",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        ),
        APIResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(schema = Schema(implementation = ServiceError::class))]
        )
    )
    suspend fun updateCategory(
        @Parameter(description = "Category name", required = true) @PathParam("categoryName") categoryName: String,
        @Parameter(description = "Category update data", required = true) request: UpdateCategoryRequest
    ): CategoryResponse

    @POST
    @Operation(summary = "Create category", description = "Creates a new category")
    @APIResponses(
        APIResponse(
            responseCode = "201",
            description = "Category successfully created",
            content = [Content(schema = Schema(implementation = CategoryResponse::class))]
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
    suspend fun createCategory(request: CreateCategoryRequest): CategoryResponse
}