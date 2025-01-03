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

        return mapOf(
            "quarkus.datasource.jdbc.url" to postgresContainer.jdbcUrl,
            "quarkus.datasource.username" to postgresContainer.username,
            "quarkus.datasource.password" to postgresContainer.password
        )
    }

    override fun stop() {
        if (::postgresContainer.isInitialized) {
            postgresContainer.stop()
        }
    }
}
