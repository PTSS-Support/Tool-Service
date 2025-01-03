package org.ptss.support.domain.interfaces.repositories

import org.ptss.support.domain.models.Category

interface ICategoryRepository {
    suspend fun getAll(): List<Category>
    suspend fun create(category: Category): String
    suspend fun delete(categoryName: String): Category?
    suspend fun update(oldCategory: String, newCategory: String): Category?
}