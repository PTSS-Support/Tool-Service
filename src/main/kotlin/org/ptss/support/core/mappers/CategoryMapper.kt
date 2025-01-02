package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.requests.categories.CreateCategoryRequest
import org.ptss.support.api.dtos.responses.categories.CategoryResponse
import org.ptss.support.domain.commands.categories.CreateCategoryCommand
import org.ptss.support.domain.models.Category

object CategoryMapper {
    fun toResponse(category: Category) = CategoryResponse(
        category = category.category,
        groupId = category.groupId,
        createdAt = category.createdAt,
        tools = category.tools.map { ToolMapper.toSummaryResponse(it) }
    )

    fun toCommand(request: CreateCategoryRequest) = CreateCategoryCommand(
        category = request.category
    )
}