package org.ptss.support.infrastructure.handlers.queries.categories

import jakarta.enterprise.context.ApplicationScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ptss.support.domain.interfaces.queries.IQueryHandler
import org.ptss.support.domain.models.Category
import org.ptss.support.domain.queries.categories.GetAllCategoriesQuery
import org.ptss.support.infrastructure.repositories.CategoryRepository

@ApplicationScoped
class GetAllCategoriesQueryHandler(
    private val categoryRepository: CategoryRepository
) : IQueryHandler<GetAllCategoriesQuery, List<Category>> {
    override suspend fun handleAsync(query: GetAllCategoriesQuery): List<Category> {
        return withContext(Dispatchers.IO) {
            categoryRepository.getAll()
        }
    }
}
