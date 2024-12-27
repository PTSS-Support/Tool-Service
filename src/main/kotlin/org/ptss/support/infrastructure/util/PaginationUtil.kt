package org.ptss.support.infrastructure.util

object PaginationUtil {
    fun <T> calculatePaginationDetails(
        items: List<T>,
        pageSize: Int,
        totalItems: Int,
        getCursor: (T) -> String
    ): Triple<List<T>, String?, Int> {
        val hasMore = items.size > pageSize
        val paginatedItems = if (hasMore) items.dropLast(1) else items
        val nextCursor = if (hasMore) getCursor(paginatedItems.last()) else null
        val totalPages = (totalItems + pageSize - 1) / pageSize

        return Triple(paginatedItems, nextCursor, totalPages)
    }
}