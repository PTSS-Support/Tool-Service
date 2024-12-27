package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.domain.models.Tool

interface IToolRepository {
    suspend fun create(tool: Tool): String
    suspend fun getAll(cursor: String?, pageSize: Int, sortOrder: String): PaginationResponse<Tool>
    suspend fun getById(id: String): Tool?
    suspend fun delete(id: String): Tool?
}