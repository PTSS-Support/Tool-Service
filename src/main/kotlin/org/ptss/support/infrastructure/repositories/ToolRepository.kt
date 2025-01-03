package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Sort
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
import java.util.*

@ApplicationScoped
class ToolRepository @Inject constructor(
    private val entityManager: EntityManager
) : IToolRepository, PanacheRepository<ToolEntity> {

    @Transactional
    override suspend fun getAll(cursor: String?, pageSize: Int, sortOrder: String): PaginationResponse<Tool> {
        val parsedCursor = cursor?.takeIf { it.isNotEmpty() }?.let { Instant.parse(it) }
        val totalItems = count().toInt()
        val sort = Sort.by("createdAt", if (sortOrder == "desc") Sort.Direction.Descending else Sort.Direction.Ascending)

        val query = when {
            parsedCursor != null -> find("createdAt ${if (sortOrder == "desc") "<" else ">"} ?1", sort, parsedCursor)
            else -> findAll(sort)
        }

        val tools = query.page(0, pageSize + 1).list()

        return CalculatePaginationDetails.calculatePaginationDetails(tools, pageSize, totalItems) { it.createdAt.toString() }.let { (items, nextCursor, totalPages) ->
            PaginationResponse(items.map { it.toDomain() }, nextCursor, items.size, totalItems, totalPages)
        }
    }

    @Transactional
    override suspend fun getById(id: String): Tool? =
        find("id", UUID.fromString(id)).firstResult()?.toDomain()

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
