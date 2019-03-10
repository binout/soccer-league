package io.github.binout.soccer

import com.github.kevinsawicki.http.HttpRequest
import org.assertj.core.api.WithAssertions
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTest: WithAssertions {

    @LocalServerPort
    var port: Int = 0

    @Test
    fun should_start() {
        assertThat(HttpRequest.get("http://localhost:$port/rest/seasons/current").ok()).isTrue()
    }

}