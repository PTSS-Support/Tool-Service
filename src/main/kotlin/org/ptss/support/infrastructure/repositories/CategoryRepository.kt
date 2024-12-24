package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.ptss.support.domain.interfaces.repositories.ICategoryRepository
import org.ptss.support.domain.models.Category
import org.ptss.support.domain.models.Comment
import org.ptss.support.infrastructure.persistence.entities.CategoryEntity
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import java.time.Instant
import java.util.UUID

@ApplicationScoped
class CategoryRepository @Inject constructor(
    private val entityManager: EntityManager
) : ICategoryRepository, PanacheRepository<CategoryEntity> {
    @Transactional
    override suspend fun getAll(): List<Category> {
        return entityManager.createQuery(
            """
            SELECT DISTINCT c 
            FROM CategoryEntity c 
            LEFT JOIN FETCH c.tools
            """, CategoryEntity::class.java
        )
            .resultList
            .map { it.toDomain() }
    }

    @Transactional
    override suspend fun update(oldCategory: String, newCategory: String): Category? {
        // Step 1: Validate and fetch the existing category
        val existingCategory = findCategoryByName(oldCategory) ?: return null

        // Step 2: Ensure the new category exists or insert it
        ensureCategoryExists(newCategory, existingCategory.groupId, existingCategory.createdAt)

        // Step 3: Update references in the join table
        updateCategoryTools(oldCategory, newCategory)

        // Step 4: Remove the old category
        deleteCategoryByName(oldCategory)

        // Step 5: Return the updated category
        return findCategoryByName(newCategory)?.toDomain()
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

        // Update bi-directional relationship if there are tools
        existingTools.forEach { tool ->
            if (!tool.categories.contains(categoryEntity)) {
                tool.categories = tool.categories + categoryEntity
            }
        }

        entityManager.persist(categoryEntity)
        entityManager.flush()
        return categoryEntity.category
    }

    private fun findCategoryByName(categoryName: String): CategoryEntity? {
        return entityManager.createQuery(
            "SELECT c FROM CategoryEntity c WHERE c.category = :category", CategoryEntity::class.java
        )
            .setParameter("category", categoryName)
            .resultList
            .firstOrNull()
    }

    private fun ensureCategoryExists(category: String, groupId: UUID, createdAt: Instant) {
        val exists = entityManager.createQuery(
            "SELECT 1 FROM CategoryEntity c WHERE c.category = :category"
        )
            .setParameter("category", category)
            .resultList
            .isNotEmpty()

        if (!exists) {
            entityManager.createNativeQuery(
                "INSERT INTO categories (category, group_id, created_at) VALUES (:category, :groupId, :createdAt)"
            )
                .setParameter("category", category)
                .setParameter("groupId", groupId)
                .setParameter("createdAt", createdAt)
                .executeUpdate()
        }
    }

    private fun updateCategoryTools(oldCategory: String, newCategory: String) {
        entityManager.createNativeQuery(
            "UPDATE category_tools SET category = :newCategory WHERE category = :oldCategory"
        )
            .setParameter("newCategory", newCategory)
            .setParameter("oldCategory", oldCategory)
            .executeUpdate()
    }

    private fun deleteCategoryByName(category: String) {
        entityManager.createNativeQuery(
            "DELETE FROM categories WHERE category = :category"
        )
            .setParameter("category", category)
            .executeUpdate()
    }
}