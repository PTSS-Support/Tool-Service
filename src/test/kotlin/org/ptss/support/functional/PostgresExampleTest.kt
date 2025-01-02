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
    fun `should return 401 Unauthorized for unauthenticated request`() {
        given()
            .`when`()
            .get("/tools")
            .then()
            .statusCode(401) // Assert that the response status is 401 Unauthorized
    }
}