package org.ptss.support.infrastructure.handlers.queries.comments

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Comment
import org.ptss.support.domain.queries.comments.GetAllCommentsQuery
import org.ptss.support.infrastructure.repositories.CommentRepository

@ApplicationScoped
class GetAllCommentsQueryHandler(
    private val commentRepository: CommentRepository
) : IQueryHandler<GetAllCommentsQuery, PaginationResponse<Comment>> {
    override suspend fun handleAsync(query: GetAllCommentsQuery): PaginationResponse<Comment> {
        return withContext(Dispatchers.IO) {
            commentRepository.getAll(query.toolId, query.cursor, query.pageSize, query.sortOrder)
        }
    }
}