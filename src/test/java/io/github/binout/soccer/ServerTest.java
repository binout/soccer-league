package io.github.binout.soccer;

import com.github.kevinsawicki.http.HttpRequest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServerTest {

    @Test
    public void should_start_and_stop() {
        Server server = new Server();

        int port = server.startOnRandomPort();
        assertThat(HttpRequest.get("http://localhost:" + port + "/rest/seasons/current").ok()).isTrue();
        server.stop();
    }

}