package org.ptss.support.functional

import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test
import org.ptss.support.core.context.PostgresContextTestContainer

@QuarkusTest
@QuarkusTestResource(PostgresContextTestContainer::class)
class PostgresExampleTest {
    @Test
    fun `test database operation`() {
        // Your test here using RestAssured
        given()
            .`when`().get("/your-endpoint")
            .then()
            .statusCode(200)
    }
}