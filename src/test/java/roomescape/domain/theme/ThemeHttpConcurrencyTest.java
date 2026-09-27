package roomescape.domain.theme;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeHttpConcurrencyTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void 동일한_이름으로_동시에_테마_생성_요청하면_하나만_201이고_나머지는_409를_응답한다() throws InterruptedException, ExecutionException {
        // given
        String token = createToken("admin@dummy.com", "dummy");

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);

        Map<String, String> params = new HashMap<>();
        params.put("name", "Dummy");
        params.put("description", "It is Dummy for Concurrency Test");

        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                readyLatch.countDown();
                startLatch.await();

                return RestAssured.given()
                        .body(params)
                        .cookie("token", token)
                        .contentType(ContentType.JSON)
                        .when().post("/themes")
                        .then()
                        .extract().statusCode();
            }));
        }

        // when
        readyLatch.await();
        startLatch.countDown();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // then
        List<Integer> statusCodes = new ArrayList<>();
        for (Future<Integer> future : futures) {
            statusCodes.add(future.get());
        }

        assertThat(statusCodes).containsExactlyInAnyOrder(
                HttpStatus.CREATED.value(),
                HttpStatus.CONFLICT.value()
        );
    }

    private String createToken(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        return RestAssured.given()
                .body(params)
                .contentType(ContentType.JSON)
                .when().post("/login")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().cookie("token");
    }
}
