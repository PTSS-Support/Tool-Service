package org.ptss.support.infrastructure.repositories

import jakarta.inject.Inject
import org.ptss.support.infrastructure.config.PostgreSQLConfig
import java.sql.Connection
import java.sql.DriverManager

abstract class BaseRepository<T> {
    @Inject
    protected lateinit var config: PostgreSQLConfig

    // Remove the lazy connection property and create a function to get connection
    protected fun getConnection(): Connection {
        return DriverManager.getConnection(
            config.url(),
            config.username(),
            config.password()
        )
    }

    protected fun <R> useConnection(block: (Connection) -> R): R {
        // Create a new connection each time
        return getConnection().use { conn ->
            block(conn)
        }
    }
}