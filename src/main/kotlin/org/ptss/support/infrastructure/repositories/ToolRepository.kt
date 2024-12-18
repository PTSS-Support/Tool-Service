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
import org.ptss.support.infrastructure.persistence.entities.ToolEntity
import org.slf4j.LoggerFactory
import java.time.Instant

@ApplicationScoped
class ToolRepository(
    private val azureConfig: AzureStorageConfig
) : IToolRepository {
    private val logger = LoggerFactory.getLogger(ToolRepository::class.java)
    private lateinit var tableClient: TableClient

    @PostConstruct
    fun initialize() {
        try {
            val tableServiceClient = TableServiceClientBuilder()
                .connectionString(azureConfig.connectionString())
                .buildClient()

            tableServiceClient.createTableIfNotExists(azureConfig.tableName())
            tableClient = tableServiceClient.getTableClient(azureConfig.tableName())
        } catch (e: Exception) {
            logger.error("Failed to initialize Azure Table Storage", e)
            throw APIException(
                errorCode = ErrorCode.SERVICE_UNAVAILABLE,
                message = "Failed to initialize storage service",
            )
        }
    }

    override fun getAll(): List<Tool> {
        return tableClient.listEntities()
            .map { entity ->
                val toolEntity = ToolEntity.fromTableEntity(entity)
                toolEntity.toDomain(entity.rowKey)
            }
            .toList()
    }
}