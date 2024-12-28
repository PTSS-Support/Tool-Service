package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import java.util.UUID

@ApplicationScoped
class ToolRepository @Inject constructor(
    private val entityManager: EntityManager
) : IToolRepository, PanacheRepository<ToolEntity> {

    @Transactional
    override suspend fun getAll(): List<Tool> {
        return entityManager
            .createQuery("SELECT DISTINCT t FROM ToolEntity t LEFT JOIN FETCH t.mediaItems", ToolEntity::class.java)
            .resultList
            .map { it.toDomain() }
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