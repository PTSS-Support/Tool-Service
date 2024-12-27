package org.ptss.support.infrastructure.handlers.queries.tools

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.Tool
import org.ptss.support.domain.queries.tools.GetAllToolsQuery

@ApplicationScoped
class GetAllToolsQueryHandler(
    private val toolRepository: IToolRepository
) : IQueryHandler<GetAllToolsQuery, PaginationResponse<Tool>> {
    override suspend fun handleAsync(query: GetAllToolsQuery): PaginationResponse<Tool> {
        return withContext(Dispatchers.IO) {
            toolRepository.getAll(query.cursor, query.pageSize, query.sortOrder)
        }
    }
}