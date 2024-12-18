package org.ptss.support.infrastructure.repositories

import com.azure.data.tables.TableClient
import com.azure.data.tables.TableServiceClientBuilder
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.common.exceptions.APIException
import org.ptss.support.domain.enums.ErrorCode
import org.ptss.support.domain.enums.MediaType
import org.ptss.support.domain.interfaces.repositories.IToolRepository
import org.ptss.support.domain.models.MediaInfo
import org.ptss.support.domain.models.Tool
import org.ptss.support.infrastructure.config.AzureStorageConfig
import org.ptss.support.infrastructure.config.PostgreSQLConfig
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.sql.DriverManager
import java.time.Instant

@ApplicationScoped
class ToolRepository(
    private val config: PostgreSQLConfig
) : IToolRepository {

    private val logger = LoggerFactory.getLogger(ToolRepository::class.java)

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