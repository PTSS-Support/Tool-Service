package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.responses.categories.CategoryResponse
import org.ptss.support.core.mappers.CategoryMapper
import org.ptss.support.core.services.CategoryService

@ApplicationScoped
class CategoryFacade @Inject constructor(
    private val categoryService: CategoryService
) {
    suspend fun getAllCategories(): List<CategoryResponse> =
        categoryService.getAllCategoriesAsync()
            .map(CategoryMapper::toResponse)
}