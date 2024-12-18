package org.ptss.support.infrastructure.repositories

import jakarta.inject.Inject
import org.ptss.support.infrastructure.config.PostgreSQLConfig
import java.sql.Connection
import java.sql.DriverManager

abstract class BaseRepository<T> {
    @Inject
    protected lateinit var config: PostgreSQLConfig

    private val connection: Connection by lazy {
        DriverManager.getConnection(config.url(), config.username(), config.password())
    }

    protected fun <R> useConnection(block: (Connection) -> R): R {
        return connection.use { conn ->
            block(conn)
        }
    }
}