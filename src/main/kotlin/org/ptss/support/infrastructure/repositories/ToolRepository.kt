package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import org.ptss.support.infrastructure.util.PaginationUtil
import java.time.Instant
import java.util.UUID

@ApplicationScoped
class ToolRepository @Inject constructor(
    private val entityManager: EntityManager
) : IToolRepository, PanacheRepository<ToolEntity> {

    @Transactional
    override suspend fun getAll(cursor: String?, pageSize: Int, sortOrder: String): PaginationResponse<Tool> {
        val parsedCursor = cursor?.takeIf { it.isNotEmpty() }?.let { Instant.parse(it) }

        // Count total items in the dataset
        val totalItems = entityManager.createQuery("SELECT COUNT(t) FROM ToolEntity t", Long::class.java)
            .singleResult
            .toInt()

        // Fetch items with pagination and cursor filtering
        val tools = entityManager.createQuery(
            """
        SELECT t
        FROM ToolEntity t
        ${parsedCursor?.let { "WHERE t.createdAt ${if (sortOrder == "desc") "<" else ">"} :cursor" } ?: ""}
        ORDER BY t.createdAt ${if (sortOrder == "desc") "DESC" else "ASC"}
        """.trimIndent(), ToolEntity::class.java
        ).apply {
            parsedCursor?.let { setParameter("cursor", it) }
            setMaxResults(pageSize + 1) // Fetch enough for the current page and one extra
        }.resultList

        // Use PaginationUtil to calculate pagination details
        val (paginatedItems, nextCursor, totalPages) = PaginationUtil.calculatePaginationDetails(
            items = tools,
            pageSize = pageSize,
            totalItems = totalItems
        ) { it.createdAt.toString() }

        // Return the response
        return PaginationResponse(
            data = paginatedItems.map { it.toDomain() },
            nextCursor = nextCursor,
            pageSize = paginatedItems.size,
            totalItems = totalItems,
            totalPages = totalPages
        )
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
        val toolEntity = entityManager
            .createQuery("SELECT t FROM ToolEntity t WHERE t.id = :id", ToolEntity::class.java)
            .setParameter("id", toolId)
            .resultList
            .firstOrNull()

        return if (toolEntity != null) {
            entityManager.remove(toolEntity)
            toolEntity.toDomain()
        } else {
            null
        }
    }

    @Transactional
    override suspend fun create(tool: Tool): String {
        val toolEntity = ToolEntity.fromDomain(tool)

        toolEntity.id = null

        entityManager.persist(toolEntity)
        entityManager.flush()
        return toolEntity.id.toString()
    }

}