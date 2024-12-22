package org.ptss.support.infrastructure.repositories

import jakarta.inject.Inject
import jakarta.persistence.EntityManager
import org.ptss.support.infrastructure.config.PostgreSQLConfig
import java.sql.Connection
import java.sql.DriverManager

abstract class BaseRepository<T> {

    @Inject
    protected lateinit var entityManager: EntityManager

    // Use EntityManager directly for database operations
    protected fun <R> useEntityManager(block: (EntityManager) -> R): R {
        return block(entityManager)
    }
}