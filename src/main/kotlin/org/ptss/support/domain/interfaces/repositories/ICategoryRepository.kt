package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Category

interface ICategoryRepository {
    suspend fun getAll(): List<Category>
}