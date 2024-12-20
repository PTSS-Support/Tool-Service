package org.ptss.support.infrastructure.repositories

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import java.sql.Timestamp

@ApplicationScoped
class ToolRepository : BaseRepository<Tool>(), IToolRepository {
    override fun getAll(): List<Tool> {
        return useConnection { conn ->
            val query = "SELECT id, name, description, created_by, created_at, category FROM tools"
            conn.prepareStatement(query).use { statement ->
                val resultSet = statement.executeQuery()
                generateSequence { if (resultSet.next()) ToolEntity.fromResultSet(resultSet) else null }
                    .map { it.toDomain() }
                    .toList()
            }
        }
    }

    override fun getById(id: String): Tool? {
        return useConnection { conn ->
            val query = "SELECT id, name, description, created_by, created_at, category FROM tools WHERE id = ?::UUID"
            conn.prepareStatement(query).use { statement ->
                statement.setString(1, id)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) ToolEntity.fromResultSet(resultSet).toDomain() else null
            }
        }
    }

    override fun delete(id: String): Tool? {
        return useConnection { conn ->
            val tool = getById(id)
            tool?.let {
                val query = "DELETE FROM tools WHERE id = ?::UUID"
                conn.prepareStatement(query).use { statement ->
                    statement.setString(1, id)
                    val rowsAffected = statement.executeUpdate()
                    if (rowsAffected > 0) tool else null
                }
            }
        }
    }

    override fun create(tool: Tool): String {
        return useConnection { conn ->
            val query = """
            INSERT INTO tools (id, name, description, created_by, created_at, category)
            VALUES (?::UUID, ?, ?, ?, ?, ?)
        """.trimIndent()

            conn.prepareStatement(query).use { statement ->
                statement.setString(1, tool.id)
                statement.setString(2, tool.name)
                statement.setString(3, tool.description)
                statement.setString(4, tool.createdBy)
                statement.setTimestamp(5, Timestamp.from(tool.createdAt))
                statement.setArray(6, conn.createArrayOf("VARCHAR", tool.category.toTypedArray()))
                statement.executeUpdate()
            }
            tool.id
        }
    }
}