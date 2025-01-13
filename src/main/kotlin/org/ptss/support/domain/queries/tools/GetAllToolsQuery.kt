package org.ptss.support.domain.queries.tools

import org.ptss.support.domain.enums.SortOrder

data class GetAllToolsQuery(
    val cursor: String?,
    val pageSize: Int,
    val sortOrder: SortOrder
)
