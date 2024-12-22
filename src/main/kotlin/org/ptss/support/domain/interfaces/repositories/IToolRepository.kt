package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Tool

interface IToolRepository {
    suspend fun create(tool: Tool): String
    suspend fun getAll(): List<Tool>
    suspend fun getById(id: String): Tool?
    suspend fun delete(id: String): Tool?
}