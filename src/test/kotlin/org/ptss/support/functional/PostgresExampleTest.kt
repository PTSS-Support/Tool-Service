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
    fun `should return 403 Forbidden for unauthenticated request`() {
        given()
            .`when`()
            .get("/tools")
            .then()
            .statusCode(403) // Assert that the response status is 403 Forbidden
    }
}