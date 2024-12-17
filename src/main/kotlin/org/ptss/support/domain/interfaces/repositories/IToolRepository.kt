package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Tool

interface IToolRepository {
    fun getAll(): List<Tool>
}