package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.responses.ToolResponse
import org.ptss.support.core.mappers.ToolMapper
import org.ptss.support.core.services.ToolService

@ApplicationScoped
class ToolFacade @Inject constructor(
    private val toolService: ToolService
) {
    suspend fun getAllTools(): List<ToolResponse> =
        toolService.getAllToolsAsync()
            .map(ToolMapper::toResponse)
}