package org.ptss.support.api.dtos.responses.pagination

data class PaginationResponse<T>(
    val data: List<T>,
    val nextCursor: String?,
    val pageSize: Int,
    val totalItems: Int,
    val totalPages: Int
)
