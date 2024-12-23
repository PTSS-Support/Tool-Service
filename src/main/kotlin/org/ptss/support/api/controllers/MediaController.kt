package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.ptss.support.api.dtos.requests.media.UploadMediaRequest
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.core.facades.MediaFacade
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.controllers.IMediaController
import org.ptss.support.security.Authentication

@Path("/tools/{toolId}/media")
@ApplicationScoped
@Consumes("multipart/form-data")
@Produces(MediaType.APPLICATION_JSON)
@Authentication(roles = [Role.ADMIN, Role.HCP])
class MediaController(
    private val mediaFacade: MediaFacade
) : IMediaController {
    override suspend fun uploadMedia(@PathParam("toolId") toolId: String, request: UploadMediaRequest): MediaInfoResponse =
        mediaFacade.uploadMedia(toolId, request)
}