package org.ptss.support.api.controllers

import com.azure.core.annotation.PathParam
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Produces
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.ptss.support.api.dtos.requests.tool.CreateToolRequest
import org.ptss.support.api.dtos.responses.tool.ToolResponse
import org.ptss.support.core.facades.ToolFacade
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.controllers.IToolController
import org.ptss.support.security.Authentication

@Path("/tools")
@ApplicationScoped
@Authentication(roles = [Role.PATIENT, Role.FAMILY_MEMBER, Role.HCP])
class ToolController(
    private val toolFacade: ToolFacade
    ) : IToolController {

    override suspend fun getAllTools(): List<ToolResponse> =
        toolFacade.getAllTools()

    override suspend fun getToolById(@PathParam("id") id: String): ToolResponse? =
        toolFacade.getToolById(id)

    override suspend fun createTool(request: CreateToolRequest): ToolResponse =
        toolFacade.createTool(request)

    override suspend fun deleteTool(@PathParam("id") id: String): Response {
        toolFacade.deleteTool(id)
        return Response.noContent().build()
    }

}