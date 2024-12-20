package org.ptss.support.infrastructure.handlers.queries.comment

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Comment
import org.ptss.support.domain.queries.comment.GetAllCommentsQuery
import org.ptss.support.infrastructure.repositories.CommentRepository

@ApplicationScoped
class GetAllCommentsQueryHandler(
    private val commentRepository: CommentRepository
) : IQueryHandler<GetAllCommentsQuery, List<Comment>> {
    override suspend fun handleAsync(query: GetAllCommentsQuery): List<Comment> {
        return commentRepository.getAll(query.toolId)
    }
}