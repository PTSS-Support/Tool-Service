package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.ptss.support.domain.interfaces.repositories.ICategoryRepository
import org.ptss.support.domain.models.Category
import org.ptss.support.infrastructure.persistence.entities.CategoryEntity
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import java.util.*

@ApplicationScoped
class CategoryRepository @Inject constructor(
    private val entityManager: EntityManager
) : ICategoryRepository, PanacheRepository<CategoryEntity> {
    @Transactional
    override suspend fun getAll(): List<Category> {
        return find("FROM CategoryEntity c LEFT JOIN FETCH c.tools")
            .list()
            .map { it.toDomain() }
    }

    @Transactional
    override suspend fun delete(categoryName: String): Category? {
        val categoryEntity = find("category", categoryName).firstResult() ?: return null

        categoryEntity.tools.forEach { tool ->
            tool.categories = tool.categories.filter { it.category != categoryEntity.category }
        }

        delete(categoryEntity)

        return categoryEntity.toDomain()
    }

    @Transactional
    override suspend fun create(category: Category): String {
        val existingTools = if (category.tools.isNotEmpty()) {
            category.tools.map { tool ->
                entityManager.find(ToolEntity::class.java, UUID.fromString(tool.id))
            }
        } else {
            emptyList()
        }

        val categoryEntity = CategoryEntity.fromDomain(category, existingTools)

        existingTools.forEach { tool ->
            if (!tool.categories.contains(categoryEntity)) {
                tool.categories = tool.categories + categoryEntity
            }
        }

        entityManager.persist(categoryEntity)
        entityManager.flush()
        return categoryEntity.category
    }

    @Transactional
    override suspend fun update(oldCategory: String, newCategory: String): Category? {
        update("category = ?1 where category = ?2", newCategory, oldCategory)
        return find("category", newCategory).firstResult()?.toDomain()
    }
}