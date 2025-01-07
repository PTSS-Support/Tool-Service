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
        val isDesc = sortOrder == "desc"
        val sort = Sort.by("id", if (isDesc) Sort.Direction.Descending else Sort.Direction.Ascending)
        val parsedCursor = cursor?.takeIf { it.isNotEmpty() }?.let { UUID.fromString(it) }
        val toolIdUUID = UUID.fromString(toolId)

        val query = when (parsedCursor) {
            null -> find("tool.id = ?1", sort, toolIdUUID)
            else -> find("tool.id = ?1 and id ${if (isDesc) "<" else ">"} ?2", sort, toolIdUUID, parsedCursor)
        }

        val comments = query.page(0, pageSize + 1).list()

        return CalculatePaginationDetails.calculatePaginationDetails(
            comments,
            pageSize,
            count("tool.id", toolIdUUID).toInt()
        ) { it.id.toString() }.let { (items, next, pages) ->
            PaginationResponse(items.map { it.toDomain() }, next, items.size, items.size, pages)
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
