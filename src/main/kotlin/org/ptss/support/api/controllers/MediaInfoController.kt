package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestForm
import org.ptss.support.api.dtos.requests.media.CreateMediaInfoRequest
import org.ptss.support.api.dtos.responses.media.MediaInfoResponse
import org.ptss.support.core.facades.MediaInfoFacade
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.controllers.IMediaInfoController
import org.ptss.support.security.Authentication
import java.io.InputStream

@Path("/tools/{toolId}/media")
@ApplicationScoped
@Authentication(roles = [Role.ADMIN, Role.HCP])
class MediaInfoController(
    private val mediaInfoFacade: MediaInfoFacade
) : IMediaInfoController {
    override suspend fun createMediaInfo(
        @PathParam("toolId") toolId: String,
        @RestForm("media") media: InputStream,
        @RestForm("href") href: String?
    ): MediaInfoResponse = mediaInfoFacade.createMediaInfo(toolId, CreateMediaInfoRequest(media, href))

    override suspend fun deleteMediaInfo(@PathParam("toolId") toolId: String, @PathParam("mediaId") mediaId: String): Response {
        mediaInfoFacade.deleteMediaInfo(toolId, mediaId)
        return Response.noContent().build()
    }
}