package org.ptss.support.infrastructure.persistence.entities

import jakarta.persistence.*
import org.ptss.support.domain.models.MediaInfo
import java.util.*

@Entity
@Table(name = "mediainfo")
class MediaInfoEntity {
    @Id
    @GeneratedValue
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    var id: UUID? = null

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    lateinit var url: String

    @Column(name = "href", nullable = true, columnDefinition = "TEXT")
    var href: String? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tool_id", nullable = false)
    lateinit var tool: ToolEntity
    
    constructor()

    constructor(id: UUID?, tool: ToolEntity, url: String, href: String?) {
        this.id = id
        this.tool = tool
        this.url = url
        this.href = href
    }

    fun toDomain() = MediaInfo(
        id = id.toString(),
        toolId = tool.id.toString(),
        url = url,
        href = href
    )

    companion object {
        fun fromDomain(mediaInfo: MediaInfo, tool: ToolEntity) = MediaInfoEntity(
            id = null,
            tool = tool,
            url = mediaInfo.url,
            href = mediaInfo.href
        )
    }
}