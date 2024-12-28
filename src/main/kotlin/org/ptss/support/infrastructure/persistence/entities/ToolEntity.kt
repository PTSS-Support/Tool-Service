package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.*
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

    @Column(name = "category", nullable = false, columnDefinition = "TEXT[]")
    var category: List<String> = emptyList()

    @Column(name = "created_by", nullable = false)
    var createdBy: String = ""

    @Column(name = "created_at", nullable = false)
    var createdAt: Instant = Instant.now()

    @OneToMany(mappedBy = "tool", cascade = [CascadeType.ALL], orphanRemoval = true)
    var mediaItems: MutableList<MediaInfoEntity> = mutableListOf()

    // No-arg constructor for JPA
    constructor()

    // Secondary constructor
    constructor(
        id: UUID?,
        name: String,
        description: String,
        category: List<String>,
        createdBy: String,
        createdAt: Instant
    ) {
        this.id = id
        this.name = name
        this.description = description
        this.category = category
        this.createdBy = createdBy
        this.createdAt = createdAt
    }

    fun toDomain() = Tool(
        id = id.toString(),
        name = name,
        description = description,
        category = category,
        createdBy = createdBy,
        createdAt = createdAt,
        media = mediaItems.map { it.toDomain() }
    )

    companion object {
        fun fromDomain(tool: Tool) = ToolEntity(
            id = if (tool.id.isNotBlank()) UUID.fromString(tool.id) else null,
            name = tool.name,
            description = tool.description,
            category = tool.category,
            createdBy = tool.createdBy,
            createdAt = tool.createdAt
        )
    }
}