package org.ptss.support.domain.queries.tools

data class GetAllToolsQuery(
    val cursor: String?,
    val pageSize: Int,
    val sortOrder: String
)
