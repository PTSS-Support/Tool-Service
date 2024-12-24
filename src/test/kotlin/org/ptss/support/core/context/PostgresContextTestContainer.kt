package org.ptss.support.core.context

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContextTestContainer : QuarkusTestResourceLifecycleManager {
    private lateinit var postgresContainer: PostgreSQLContainer<*>

    override fun start(): Map<String, String> {
        postgresContainer = PostgreSQLContainer(DockerImageName.parse("postgres:15-alpine"))
            .withDatabaseName("test_database")
            .withUsername("test")
            .withPassword("test")

        postgresContainer.start()

        // Create schema after container starts
        postgresContainer.createConnection("").use { connection ->
            connection.createStatement().execute("CREATE SCHEMA IF NOT EXISTS tool_service")
        }

        return mapOf(
            "quarkus.datasource.jdbc.url" to postgresContainer.jdbcUrl,
            "quarkus.datasource.username" to postgresContainer.username,
            "quarkus.datasource.password" to postgresContainer.password,
            "quarkus.hibernate-orm.database.default-schema" to "tool_service"
        )
    }

    override fun stop() {
        if (::postgresContainer.isInitialized) {
            postgresContainer.stop()
        }
    }
}
