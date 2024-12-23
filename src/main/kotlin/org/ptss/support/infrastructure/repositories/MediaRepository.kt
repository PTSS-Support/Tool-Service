package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.ptss.support.domain.interfaces.repositories.IMediaRepository
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.infrastructure.persistence.entities.MediaEntity
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import java.util.UUID

@ApplicationScoped
class MediaRepository @Inject constructor(
    private val entityManager: EntityManager
) : IMediaRepository, PanacheRepository<MediaEntity> {

    @Transactional
    override suspend fun upload(media: MediaInfo): MediaInfo {
        // Find the associated tool
        val tool = entityManager.find(ToolEntity::class.java, UUID.fromString(media.toolId))

        // Create and persist the media entity
        val entity = MediaEntity.fromDomain(media, tool)
        entity.id = null // Ensure ID is null for auto-generation

        entityManager.persist(entity)

        // Add the media to the tool's collection
        tool.mediaItems.add(entity)

        entityManager.flush()
        return entity.toDomain()
    }
}