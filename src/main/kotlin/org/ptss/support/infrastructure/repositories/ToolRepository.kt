package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.persistence.entities.CategoryEntity
import org.ptss.support.infrastructure.persistence.entities.ToolEntity

import org.ptss.support.infrastructure.util.CalculatePaginationDetails
import java.time.Instant
import java.util.UUID

@ApplicationScoped
class ToolRepository @Inject constructor(
    private val entityManager: EntityManager
) : IToolRepository, PanacheRepository<ToolEntity> {

    @Transactional
    override suspend fun getAll(): List<Tool> {
        return entityManager
            .createQuery("SELECT DISTINCT t FROM ToolEntity t LEFT JOIN FETCH t.mediaItem", ToolEntity::class.java)
            .resultList
            .map { it.toDomain() 
            }
    }

    @Transactional
    override suspend fun getById(id: String): Tool? {
        val toolId = UUID.fromString(id)
        return entityManager
            .createQuery("SELECT t FROM ToolEntity t WHERE t.id = :id", ToolEntity::class.java)
            .setParameter("id", toolId)
            .resultList
            .firstOrNull()
            ?.toDomain()
    }

    @Transactional
    override suspend fun delete(id: String): Tool? {
        val toolId = UUID.fromString(id)
        val toolEntity = find("id", toolId).firstResult() ?: return null

        // Clear tool references from categories first
        toolEntity.categories.forEach { category ->
            category.tools = category.tools.filter { it.id != toolEntity.id }
        }

        delete("id", toolId)
        return toolEntity.toDomain()
    }

    @Transactional
    override suspend fun create(tool: Tool): String {
        val categoryEntities = tool.category.map { categoryName ->
            entityManager.find(CategoryEntity::class.java, categoryName)
        }

        val toolEntity = ToolEntity.fromDomain(tool, categoryEntities)
        toolEntity.id = null

        // Update bi-directional relationship
        categoryEntities.forEach { category ->
            if (!category.tools.contains(toolEntity)) {
                category.tools = category.tools + toolEntity
            }
        }

        entityManager.persist(toolEntity)
        entityManager.flush()
        return toolEntity.id.toString()
    }

}
