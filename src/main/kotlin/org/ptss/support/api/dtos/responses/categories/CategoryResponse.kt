package org.ptss.support.api.dtos.responses.categories

import org.ptss.support.api.dtos.responses.tools.ToolSummaryResponse
import java.time.Instant

data class CategoryResponse(
    val category: String,
    val createdAt: Instant,
    val groupId: String,
    val tools: List<ToolSummaryResponse>
)
