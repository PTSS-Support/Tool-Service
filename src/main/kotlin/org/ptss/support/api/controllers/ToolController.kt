package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.ptss.support.api.dtos.responses.ToolResponse
import org.ptss.support.core.facades.ToolFacade
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.controllers.IToolController
import org.ptss.support.security.Authentication

@Path("/tools")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
//@Authentication(roles = [Role.PATIENT, Role.FAMILY_MEMBER, Role.HCP])
class ToolController(
    private val toolFacade: ToolFacade
    ) : IToolController {

    @GET
    override suspend fun getAllTools(): List<ToolResponse> =
        toolFacade.getAllTools()
}