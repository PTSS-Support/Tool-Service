package org.ptss.support.infrastructure.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
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

        val comments = find(
            "tool.id = ?1 ${parsedCursor?.let { "AND createdAt ${if (sortOrder == "desc") "<" else ">"} ?2" } ?: ""} " +
                    "ORDER BY createdAt ${if (sortOrder == "desc") "DESC" else "ASC"}",
            *listOfNotNull(toolIdUUID, parsedCursor).toTypedArray()
        ).page(0, pageSize + 1).list()

        return CalculatePaginationDetails.calculatePaginationDetails(comments, pageSize, totalItems) { it.createdAt.toString() }
            .let { (items, nextCursor, totalPages) ->
                PaginationResponse(items.map { it.toDomain() }, nextCursor, items.size, totalItems, totalPages)
            }
    }

    @Transactional
    override suspend fun create(comment: Comment): String {
        val toolEntity = entityManager.find(ToolEntity::class.java, UUID.fromString(comment.toolId))
            ?: throw IllegalArgumentException("Tool with ID ${comment.toolId} does not exist")

        val entity = CommentEntity.fromDomain(comment, toolEntity)

        // Ensure the ID is null before persisting
        entity.id = null

        entityManager.persist(entity)
        entityManager.flush()
        return entity.id.toString()
    }

    @Transactional
    override suspend fun update(toolId: String, commentId: String, content: String): Comment? {
        val entity = findCommentByToolAndId(toolId, commentId) ?: return null

        entity.content = content
        entity.lastEditedAt = java.time.Instant.now()
        entityManager.merge(entity)

        return entity.toDomain()
    }

    @Transactional
    override suspend fun delete(toolId: String, commentId: String): Comment? {
        val entity = findCommentByToolAndId(toolId, commentId) ?: return null

        entityManager.remove(entity)
        return entity.toDomain()
    }

    // Helper function to find a comment by toolId and commentId
    private fun findCommentByToolAndId(toolId: String, commentId: String): CommentEntity? {
        val toolIdUUID = UUID.fromString(toolId)
        val commentIdUUID = UUID.fromString(commentId)

        return entityManager.createQuery(
            """
            SELECT c 
            FROM CommentEntity c 
            JOIN c.tool t 
            WHERE t.id = :toolId AND c.id = :commentId
            """, CommentEntity::class.java
        )
            .setParameter("toolId", toolIdUUID)
            .setParameter("commentId", commentIdUUID)
            .resultList
            .firstOrNull()
    }
}
