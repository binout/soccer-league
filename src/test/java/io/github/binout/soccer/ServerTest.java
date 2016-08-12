package io.github.binout.soccer;

import com.github.kevinsawicki.http.HttpRequest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerTest {

    @Test
    public void should_start() {
        Server.main(new String[]{});

        assertThat(HttpRequest.get("http://localhost:8080/rest/seasons/current").ok()).isTrue();
    }

}