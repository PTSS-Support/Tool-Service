package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.*
import org.ptss.support.domain.models.MediaInfo
import java.util.*

@Entity
class MediaInfoEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    var id: UUID? = null

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    lateinit var url: String

    @Column(name = "href", nullable = true, columnDefinition = "TEXT") // Updated
    var href: String? = null

    // Many-to-one relationship with ToolEntity
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = false)
    lateinit var tool: ToolEntity

    // No-arg constructor for JPA
    constructor()

    // Constructor for creating the entity
    constructor(id: UUID?, tool: ToolEntity, url: String, href: String?) { // Updated
        this.id = id
        this.tool = tool
        this.url = url
        this.href = href
    }

    fun toDomain() = MediaInfo(
        id = id.toString(),
        toolId = tool.id.toString(),
        url = url,
        href = href // Updated
    )

    companion object {
        fun fromDomain(mediaInfo: MediaInfo, tool: ToolEntity) = MediaInfoEntity(
            id = null,
            tool = tool,
            url = mediaInfo.url,
            href = mediaInfo.href // Updated
        )
    }
}