package org.ptss.support.core.mappers

import org.ptss.support.api.dtos.responses.categories.CategoryResponse
import org.ptss.support.domain.models.Category

object CategoryMapper {
    fun toResponse(category: Category) = CategoryResponse(
        category = category.category, // Primary key
        groupId = category.groupId,
        createdAt = category.createdAt,
        tools = category.tools.map { ToolMapper.toResponse(it) }
    )
}