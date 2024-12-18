package org.ptss.support.infrastructure.persistence.entities

import com.azure.data.tables.models.TableEntity
import org.ptss.support.domain.enums.MediaType
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.domain.models.Tool
import java.time.Instant

data class ToolEntity(
    val id: String,
    val name: String,
    val description: String,
    val createdBy: String,
    val createdAt: Instant
) {
    fun toDomain(): Tool {
        return Tool(
            id = id,
            name = name,
            description = description,
            //media = emptyList(), // Media handling removed for now
            createdBy = createdBy,
            createdAt = createdAt
        )
    }
}