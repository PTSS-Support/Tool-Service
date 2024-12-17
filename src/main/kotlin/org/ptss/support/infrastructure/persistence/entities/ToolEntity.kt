package org.ptss.support.infrastructure.persistence.entities
/*
import com.azure.data.tables.models.TableEntity
import org.ptss.support.domain.models.Tool
import java.time.Instant

data class ToolEntity(
    var name: String = "",
    var description: String = "",
    var media: String = "",
    var createdBy: String = "",
    var createdAt: Instant = Instant.now()
) {
    fun toTableEntity(tool: Tool): TableEntity {
        return TableEntity("TOOL", tool.id).apply {
            properties.apply {
                put("name", name)
                put("description", description)
                put("media", media)
                put("createdBy", createdBy)
                put("createdAt", createdAt.toString())
            }
        }
    }

    companion object {
        fun fromTableEntity(entity: TableEntity): ToolEntity {
            return ToolEntity(
                name = entity.properties["name"] as String,
                description = entity.properties["description"] as String,
                media = entity.properties["media"] as String,
                createdBy = entity.properties["createdBy"] as String,
                createdAt = Instant.parse(entity.properties["createdAt"] as String)
            )
        }
    }

    fun toDomain(): Tool {
        return Tool(
            id = "", // This should come from the TableEntity row key
            name = name,
            description = description,
            media = media.split(",").filter { it.isNotEmpty() },
            createdBy = createdBy,
            createdAt = createdAt
        )
    }
)}
*/