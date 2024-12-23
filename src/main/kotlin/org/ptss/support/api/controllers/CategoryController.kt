package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import org.ptss.support.api.dtos.responses.categories.CategoryResponse
import org.ptss.support.core.facades.CategoryFacade
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.controllers.ICategoryController
import org.ptss.support.security.Authentication

@Path("/tools/categories")
@ApplicationScoped
@Authentication(roles = [Role.PATIENT, Role.FAMILY_MEMBER, Role.HCP])
class CategoryController(
    private val categoryFacade: CategoryFacade
) : ICategoryController {
    override suspend fun getAllCategories(): List<CategoryResponse> =
        categoryFacade.getAllCategories()
}