package org.ptss.support.domain.queries.comments

import org.ptss.support.domain.enums.SortOrder

data class GetAllCommentsQuery(
    val toolId: String,
    val cursor: String?,
    val pageSize: Int,
    val sortOrder: SortOrder
)
