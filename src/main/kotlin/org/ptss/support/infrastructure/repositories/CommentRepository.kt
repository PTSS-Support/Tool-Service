package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.ptss.support.api.dtos.responses.pagination.PaginationResponse
import org.ptss.support.domain.interfaces.repositories.ICommentRepository
import org.ptss.support.domain.models.Comment
import org.ptss.support.infrastructure.persistence.entities.CommentEntity
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import org.ptss.support.infrastructure.util.CalculatePaginationDetails
import java.time.Instant
import java.util.*

@ApplicationScoped
class CommentRepository @Inject constructor(
    private val entityManager: EntityManager
) : ICommentRepository, PanacheRepository<CommentEntity> {

    @Transactional
    override suspend fun getAll(toolId: String, cursor: String?, pageSize: Int, sortOrder: String): PaginationResponse<Comment> {
        val toolIdUUID = UUID.fromString(toolId)
        val parsedCursor = cursor?.let(Instant::parse)
        val totalItems = count("tool.id", toolIdUUID).toInt()

        val sort = Sort.by("createdAt", if (sortOrder == "desc") Sort.Direction.Descending else Sort.Direction.Ascending)

        val query = find("tool.id = ?1", sort, toolIdUUID)

        val comments = query.list()
            .filter { parsedCursor == null || (sortOrder == "desc" && it.createdAt < parsedCursor) || (sortOrder != "desc" && it.createdAt > parsedCursor) }
            .take(pageSize + 1)

        return CalculatePaginationDetails.calculatePaginationDetails(comments, pageSize, totalItems) { it.createdAt.toString() }
            .let { (items, nextCursor, totalPages) ->
                PaginationResponse(items.map { it.toDomain() }, nextCursor, items.size, totalItems, totalPages)
            }
    }


    @Transactional
    override suspend fun create(comment: Comment): String {
        val toolEntity = entityManager.find(ToolEntity::class.java, UUID.fromString(comment.toolId))

        val entity = CommentEntity.fromDomain(comment, toolEntity)

        entity.id = null

        entityManager.persist(entity)
        entityManager.flush()
        return entity.id.toString()
    }

    @Transactional
    override suspend fun update(toolId: String, commentId: String, content: String): Comment? {
        val entity = findCommentByToolAndId(toolId, commentId) ?: return null

        entity.content = content
        entity.lastEditedAt = Instant.now()

        persist(entity)

        return entity.toDomain()
    }

    @Transactional
    override suspend fun delete(toolId: String, commentId: String): Comment? {
        val entity = findCommentByToolAndId(toolId, commentId) ?: return null

        entityManager.remove(entity)
        return entity.toDomain()
    }
    
    private fun findCommentByToolAndId(toolId: String, commentId: String): CommentEntity? {
        val toolIdUUID = UUID.fromString(toolId)
        val commentIdUUID = UUID.fromString(commentId)

        return find("tool.id = ?1 and id = ?2", toolIdUUID, commentIdUUID).firstResult()
    }
}
