package org.ptss.support.infrastructure.persistence.entities

import org.ptss.support.domain.models.Tool
import java.sql.ResultSet
import java.time.Instant

data class ToolEntity(
    val id: String,
    val name: String,
    val description: String,
    val category: List<String>,
    val createdBy: String,
    val createdAt: Instant
) {
    fun toDomain(): Tool {
        return Tool(
            id = id,
            name = name,
            description = description,
            category = category,
            createdBy = createdBy,
            createdAt = createdAt,
            media = emptyList()
        )
    }

    companion object {
        fun fromResultSet(resultSet: ResultSet): ToolEntity {
            return ToolEntity(
                id = resultSet.getString("id"),
                name = resultSet.getString("name"),
                description = resultSet.getString("description"),
                createdBy = resultSet.getString("created_by"),
                createdAt = resultSet.getTimestamp("created_at").toInstant(),
                category = resultSet.getArray("category")?.let { array ->
                    (array.array as Array<String>).toList()
                } ?: emptyList()
            )
        }
    }
}