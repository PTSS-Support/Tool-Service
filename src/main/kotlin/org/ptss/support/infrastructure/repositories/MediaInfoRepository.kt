package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
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
        val tool = entityManager.find(ToolEntity::class.java, toolId)

        val mediaEntity = MediaInfoEntity.fromDomain(mediaInfo, tool)

        persist(mediaEntity)

        return mediaEntity.toDomain()
    }

    @Transactional
    override suspend fun delete(toolId: String, mediaId: String): MediaInfo? {
        val mediaEntity = find("id = ?1 AND tool.id = ?2", UUID.fromString(mediaId), UUID.fromString(toolId))
            .firstResult() ?: return null

        mediaEntity.tool.mediaItem = null

        delete(mediaEntity)

        return mediaEntity.toDomain()
    }

    @Transactional
    override suspend fun hasExistingMedia(toolId: String): Boolean {
        return count("tool.id = ?1", UUID.fromString(toolId)) > 0
    }
}