package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import jakarta.ws.rs.NotFoundException
import org.ptss.support.domain.interfaces.repositories.IMediaInfoRepository
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.infrastructure.persistence.entities.MediaInfoEntity
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import java.util.*

@ApplicationScoped
class MediaInfoRepository @Inject constructor(
    private val entityManager: EntityManager,
) : IMediaInfoRepository, PanacheRepository<MediaInfoEntity> {

    @Transactional
    override suspend fun create(mediaInfo: MediaInfo): MediaInfo {
        val toolId = UUID.fromString(mediaInfo.toolId)
        val tool = entityManager
            .createQuery("SELECT t FROM ToolEntity t WHERE t.id = :id", ToolEntity::class.java)
            .setParameter("id", toolId)
            .resultList
            .firstOrNull() ?: throw NotFoundException("Tool not found with id: ${mediaInfo.toolId}")

        // Create new entity without ID
        val mediaEntity = MediaInfoEntity.fromDomain(mediaInfo, tool)

        // Persist and flush
        entityManager.persist(mediaEntity)
        entityManager.flush()

        // Return domain model with generated ID
        return mediaEntity.toDomain()
    }

    @Transactional
    override suspend fun delete(toolId: String, mediaId: String): MediaInfo? {
        val mediaEntity = entityManager
            .createQuery(
                "SELECT m FROM MediaInfoEntity m WHERE m.id = :id AND m.tool.id = :toolId",
                MediaInfoEntity::class.java
            )
            .setParameter("id", UUID.fromString(mediaId))
            .setParameter("toolId", UUID.fromString(toolId))
            .resultList
            .firstOrNull()
            ?: return null

        entityManager.remove(mediaEntity)
        return mediaEntity.toDomain()
    }

    @Transactional
    override suspend fun hasExistingMedia(toolId: String): Boolean {
        val count = entityManager
            .createQuery(
                "SELECT COUNT(m) FROM MediaInfoEntity m WHERE m.tool.id = :toolId",
                Long::class.java
            )
            .setParameter("toolId", UUID.fromString(toolId))
            .singleResult

        return count > 0
    }
}