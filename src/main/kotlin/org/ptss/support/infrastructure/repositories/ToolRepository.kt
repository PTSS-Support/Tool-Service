package org.ptss.support.infrastructure.repositories

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.persistence.entities.ToolEntity

@ApplicationScoped
class ToolRepository : BaseRepository<Tool>(), IToolRepository {
    override fun getAll(): List<Tool> {
        return useConnection { conn ->
            val tools = mutableListOf<ToolEntity>()
            val query = "SELECT id, name, description, created_by, created_at FROM tools"

            conn.prepareStatement(query).use { statement ->
                val resultSet = statement.executeQuery()
                while (resultSet.next()) {
                    tools.add(
                        ToolEntity(
                            id = resultSet.getString("id"),
                            name = resultSet.getString("name"),
                            description = resultSet.getString("description"),
                            createdBy = resultSet.getString("created_by"),
                            createdAt = resultSet.getTimestamp("created_at").toInstant()
                        )
                    )
                }
            }

            tools.map { it.toDomain() }
        }
    }
}