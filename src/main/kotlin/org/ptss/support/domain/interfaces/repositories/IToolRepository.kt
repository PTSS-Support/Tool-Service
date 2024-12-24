package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Tool

interface IToolRepository {
    fun create(tool: Tool): String
    fun getAll(): List<Tool>
    fun getById(id: String): Tool?
    fun delete(id: String): Tool?
}