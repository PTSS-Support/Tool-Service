package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import org.ptss.support.api.dtos.requests.media.CreateMediaInfoRequest
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.core.facades.MediaInfoFacade
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.controllers.IMediaInfoController
import org.ptss.support.security.Authentication

@Path("/tools/{toolId}/media")
@ApplicationScoped
@Authentication(roles = [Role.ADMIN, Role.HCP])
class MediaInfoController(
    private val mediaInfoFacade: MediaInfoFacade
) : IMediaInfoController {
    override suspend fun createMediaInfo(@PathParam("toolId") toolId: String, request: CreateMediaInfoRequest): MediaInfoResponse =
        mediaInfoFacade.createMediaInfo(toolId, request)
}