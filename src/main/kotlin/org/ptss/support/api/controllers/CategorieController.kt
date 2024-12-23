package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Path
import org.ptss.support.domain.enums.Role
import org.ptss.support.security.Authentication

@Path("/tools/{toolId}/categories")
@ApplicationScoped
@Authentication(roles = [Role.PATIENT, Role.FAMILY_MEMBER, Role.HCP])
class CategorieController {
    override suspend fun getAllCategories(): List<CategorieResponse> =
        categorieFacade.getAllCategories()
}