package org.ptss.support.api.controllers

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.ptss.support.api.dtos.requests.comment.CreateCommentRequest
import org.ptss.support.api.dtos.requests.comment.UpdateCommentRequest
import org.ptss.support.api.dtos.responses.comment.CommentResponse
import org.ptss.support.api.dtos.responses.tool.ToolResponse
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

    override suspend fun getAllComments(@PathParam("toolId") toolId: String): List<CommentResponse> =
        commentFacade.getAllComments(toolId)

    override suspend fun createComment(@PathParam("toolId") toolId: String, request: CreateCommentRequest): CommentResponse =
        commentFacade.createComment(toolId, request)

    override suspend fun updateComment(
        @PathParam("toolId") toolId: String,
        @PathParam("commentId") commentId: String,
        request: UpdateCommentRequest
    ): CommentResponse =
        commentFacade.updateComment(toolId, commentId, request)

    override suspend fun deleteComment(@PathParam("toolId") toolId: String, @PathParam("commentId") commentId: String): Response {
        commentFacade.deleteComment(toolId, commentId)
        return Response.noContent().build()
    }
}