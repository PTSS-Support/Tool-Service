package org.ptss.support.domain.commands.categories

data class UpdateCategoryCommand(
    val oldCategory: String,
    val newCategory: String
)
