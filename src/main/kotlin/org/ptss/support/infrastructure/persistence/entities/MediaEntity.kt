package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.*
import org.ptss.support.domain.enums.MediaType
import org.ptss.support.domain.models.MediaInfo
import java.util.UUID

@Entity
@Table(name = "media")
class MediaEntity {

    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    var id: UUID? = null

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    lateinit var url: String

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    lateinit var type: MediaType

    // Many-to-one relationship with ToolEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = false)
    lateinit var tool: ToolEntity

    // No-arg constructor for JPA
    constructor()

    // Constructor for creating the entity
    constructor(id: UUID?, url: String, type: MediaType, tool: ToolEntity) {
        this.id = id
        this.url = url
        this.type = type
        this.tool = tool
    }

    fun toDomain() = MediaInfo(
        id = id.toString(),
        url = url,
        type = type,
        toolId = tool.id.toString()
    )

    companion object {
        fun fromDomain(media: MediaInfo, tool: ToolEntity) = MediaEntity(
            id = if (media.id.isNotBlank()) UUID.fromString(media.id) else null,
            url = media.url,
            type = media.type,
            tool = tool
        )
    }
}