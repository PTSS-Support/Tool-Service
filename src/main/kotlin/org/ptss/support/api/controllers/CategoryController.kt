package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import org.ptss.support.api.dtos.requests.categories.CreateCategoryRequest
import org.ptss.support.api.dtos.requests.categories.UpdateCategoryRequest
import org.ptss.support.api.dtos.requests.comments.UpdateCommentRequest
import org.ptss.support.api.dtos.responses.categories.CategoryResponse
import org.ptss.support.api.dtos.responses.comments.CommentResponse
import org.ptss.support.core.facades.CategoryFacade
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.controllers.ICategoryController
import org.ptss.support.security.Authentication

@Path("/tools/categories")
@ApplicationScoped
//@Authentication(roles = [Role.PATIENT, Role.FAMILY_MEMBER, Role.HCP])
class CategoryController(
    private val categoryFacade: CategoryFacade
) : ICategoryController {
    override suspend fun getAllCategories(): List<CategoryResponse> =
        categoryFacade.getAllCategories()

    override suspend fun createCategory(request: CreateCategoryRequest): CategoryResponse =
        categoryFacade.createCategory(request)

    override suspend fun updateCategory(
        @PathParam("categoryName") categoryName: String,
        request: UpdateCategoryRequest
    ): CategoryResponse =
        categoryFacade.updateCategory(categoryName, request)
}