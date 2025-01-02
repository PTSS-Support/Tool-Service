package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Column
import jakarta.persistence.ManyToMany
import org.ptss.support.domain.models.Tool
import java.time.Instant
import java.util.*

@Entity
@Table(name = "tools")
class ToolEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false)
    var id: UUID? = null

    @Column(name = "user_id", nullable = false)
    var userId: UUID? = null

    @Column(nullable = false, columnDefinition = "TEXT")
    var name: String = ""

    @Column(nullable = false, columnDefinition = "TEXT")
    var description: String = ""

    @ManyToMany(mappedBy = "tools", targetEntity = CategoryEntity::class)
    var categories: List<CategoryEntity> = emptyList()

    @Column(name = "created_by", nullable = false)
    var createdBy: String = ""

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now()

    @OneToOne(mappedBy = "tool", cascade = [CascadeType.ALL], orphanRemoval = true)
    var mediaItem: MediaInfoEntity? = null // Single object, nullable

    // No-arg constructor for JPA
    constructor()

    constructor(
        id: UUID?,
        userId: UUID?,
        name: String,
        description: String,
        categories: List<CategoryEntity>,
        createdBy: String,
        createdAt: Instant
    ) {
        this.id = id
        this.userId = userId
        this.name = name
        this.description = description
        this.categories = categories
        this.createdBy = createdBy
        this.createdAt = createdAt
    }

    fun toDomain() = Tool(
        id = id.toString(),
        userId = userId.toString(),
        name = name,
        description = description,
        category = categories.map { it.category },
        createdBy = createdBy,
        createdAt = createdAt,
        media = mediaItem?.toDomain() // Nullable mapping
    )

    companion object {
        fun fromDomain(tool: Tool, categories: List<CategoryEntity>) = ToolEntity(
            id = if (tool.id.isNotBlank()) UUID.fromString(tool.id) else null,
            userId = UUID.fromString(tool.userId),
            name = tool.name,
            description = tool.description,
            categories = categories,
            createdBy = tool.createdBy,
            createdAt = tool.createdAt
        )
    }
}

