package org.ptss.support.infrastructure.config

import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithDefault
import io.smallrye.config.WithName

@ConfigMapping(prefix = "azure.storage")
interface AzureStorageConfig {
    @WithName("connection-string")
    fun connectionString(): String

    @WithName("table-name")
    fun tableName(): String

    @WithName("container-name")
    @WithDefault("media")
    fun containerName(): String
}
