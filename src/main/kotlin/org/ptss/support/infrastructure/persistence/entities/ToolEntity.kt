package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.Entity
import jakarta.persistence.Table
import jakarta.persistence.Id
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Column
import jakarta.persistence.ManyToMany
import org.ptss.support.domain.models.Tool
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "tools")
class ToolEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    var id: UUID? = null

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

    constructor()

    constructor(
        id: UUID?,
        name: String,
        description: String,
        categories: List<CategoryEntity>,
        createdBy: String,
        createdAt: Instant
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.categories = categories
        this.createdBy = createdBy
        this.createdAt = createdAt
    }

    fun toDomain() = Tool(
        id = id.toString(),
        name = name,
        description = description,
        category = categories.map { it.category }, // Map category names
        createdBy = createdBy,
        createdAt = createdAt,
        media = emptyList()
    )

    companion object {
        fun fromDomain(tool: Tool, categories: List<CategoryEntity>) = ToolEntity(
            id = if (tool.id.isNotBlank()) UUID.fromString(tool.id) else null,
            name = tool.name,
            description = tool.description,
            categories = categories,
            createdBy = tool.createdBy,
            createdAt = tool.createdAt
        )
    }
}

