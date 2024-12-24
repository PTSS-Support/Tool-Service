package org.ptss.support.core.facades

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.ptss.support.api.dtos.requests.categories.CreateCategoryRequest
import org.ptss.support.api.dtos.requests.categories.UpdateCategoryRequest
import org.ptss.support.api.dtos.responses.categories.CategoryResponse
import org.ptss.support.core.mappers.CategoryMapper
import org.ptss.support.core.services.CategoryService
import org.ptss.support.domain.commands.categories.UpdateCategoryCommand

@ApplicationScoped
class CategoryFacade @Inject constructor(
    private val categoryService: CategoryService
) {
    suspend fun getAllCategories(): List<CategoryResponse> =
        categoryService.getAllCategoriesAsync()
            .map(CategoryMapper::toResponse)

    suspend fun createCategory(request: CreateCategoryRequest): CategoryResponse {
        val category = categoryService.createCategoryAsync(CategoryMapper.toCommand(request))
        return CategoryMapper.toResponse(category)
    }

    suspend fun updateCategory(categoryName: String, request: UpdateCategoryRequest): CategoryResponse {
        val command = UpdateCategoryCommand(categoryName, request.category)
        val updatedCategory = categoryService.updateCategoryAsync(categoryName, command)
        return CategoryMapper.toResponse(updatedCategory)
    }
}