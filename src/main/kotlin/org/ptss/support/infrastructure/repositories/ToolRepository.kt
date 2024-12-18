package org.ptss.support.infrastructure.repositories

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.config.PostgreSQLConfig
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import java.sql.Connection
import java.sql.DriverManager

@ApplicationScoped
class ToolRepository(
    private val config: PostgreSQLConfig
) : IToolRepository {

    private val connection: Connection by lazy {
        DriverManager.getConnection(config.url(), config.username(), config.password())
    }

    override fun getAll(): List<Tool> {
        val tools = mutableListOf<ToolEntity>()
        val query = "SELECT id, name, description, created_by, created_at FROM tools"

        connection.prepareStatement(query).use { statement ->
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
        return tools.map { it.toDomain() }
    }
}