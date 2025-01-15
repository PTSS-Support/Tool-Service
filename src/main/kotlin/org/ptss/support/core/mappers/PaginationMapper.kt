package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.responses.pagination.PaginationResponse

object PaginationMapper {
    fun <T, R> mapPaginationResponse(
        paginationResponse: PaginationResponse<T>,
        transform: (T) -> R
    ): PaginationResponse<R> {
        return PaginationResponse(
            data = paginationResponse.data.map(transform),
            nextCursor = paginationResponse.nextCursor,
            pageSize = paginationResponse.pageSize,
            totalItems = paginationResponse.totalItems,
            totalPages = paginationResponse.totalPages
        )
    }
}
