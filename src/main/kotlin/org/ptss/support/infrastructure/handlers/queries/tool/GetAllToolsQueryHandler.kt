package org.ptss.support.infrastructure.handlers.queries.tool

import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.Tool
import org.ptss.support.domain.queries.GetAllToolsQuery
@ApplicationScoped
@RegisterForReflection
class GetAllToolsQueryHandler(
    private val toolRepository: IToolRepository
) : IQueryHandler<GetAllToolsQuery, List<Tool>> {
    override suspend fun handleAsync(query: GetAllToolsQuery): List<Tool> {
        return toolRepository.getAll()
    }
}