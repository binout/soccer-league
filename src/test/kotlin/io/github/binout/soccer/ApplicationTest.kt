package io.github.binout.soccer

import com.github.kevinsawicki.http.HttpRequest
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest(@LocalServerPort val port: Int): WithAssertions {

    @Test
    fun should_start() {
        assertThat(HttpRequest.get("http://localhost:$port/rest/seasons/current").ok()).isTrue()
    }

}