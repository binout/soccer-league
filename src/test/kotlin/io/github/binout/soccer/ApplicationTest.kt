package io.github.binout.soccer

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test

@QuarkusTest
class ApplicationTest: WithAssertions {

    @Test
    fun should_start() {
        given()
                .`when`().get("/rest/seasons/current")
                .then()
                .statusCode(200)
    }

}