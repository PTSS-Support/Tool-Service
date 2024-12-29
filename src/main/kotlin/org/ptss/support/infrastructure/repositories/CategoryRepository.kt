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
    override suspend fun delete(categoryName: String): Category? {
        val category = findCategoryByName(categoryName) ?: return null

        // Remove tool associations first
        entityManager.createNativeQuery(
            "DELETE FROM category_tools WHERE category = :category"
        )
            .setParameter("category", categoryName)
            .executeUpdate()

        // Delete the category
        entityManager.createNativeQuery(
            "DELETE FROM categories WHERE category = :category"
        )
            .setParameter("category", categoryName)
            .executeUpdate()

        return category.toDomain()
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

    @Transactional
    override suspend fun update(oldCategory: String, newCategory: String): Category? {
        // Step 1: Fetch the existing category
        val existingCategory = findCategoryByName(oldCategory) ?: return null

        // Step 2: Detach the entity from the persistence context
        entityManager.detach(existingCategory)

        // Step 3: Perform the update in the database
        entityManager.createNativeQuery(
            "UPDATE categories SET category = :newCategory WHERE category = :oldCategory"
        )
            .setParameter("newCategory", newCategory)
            .setParameter("oldCategory", oldCategory)
            .executeUpdate()

        // Step 4: Update the detached entity
        existingCategory.category = newCategory

        // Step 5: Merge the updated entity back into the persistence context
        val mergedEntity = entityManager.merge(existingCategory)

        return mergedEntity.toDomain()
    }


    private fun findCategoryByName(categoryName: String): CategoryEntity? {
        return entityManager.createQuery(
            "SELECT c FROM CategoryEntity c WHERE c.category = :category", CategoryEntity::class.java
        )
            .setParameter("category", categoryName)
            .resultList
            .firstOrNull()
    }
}