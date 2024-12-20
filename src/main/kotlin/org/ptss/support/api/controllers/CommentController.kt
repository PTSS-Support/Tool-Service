package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.ptss.support.api.dtos.requests.comment.CreateCommentRequest
import org.ptss.support.api.dtos.responses.comment.CommentResponse
import org.ptss.support.core.facades.CommentFacade
import org.ptss.support.domain.enums.Role
import org.ptss.support.domain.interfaces.controllers.ICommentController
import org.ptss.support.security.Authentication

@Path("/tools/{toolId}/comments")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Authentication(roles = [Role.PATIENT, Role.FAMILY_MEMBER, Role.HCP])
class CommentController(
    private val commentFacade: CommentFacade
) : ICommentController {

    override suspend fun createComment(
        @PathParam("toolId") toolId: String,  // Capture the toolId from the path
        request: CreateCommentRequest
    ): CommentResponse {
        // Pass the toolId along with the request to the facade
        return commentFacade.createComment(toolId, request)
    }
}