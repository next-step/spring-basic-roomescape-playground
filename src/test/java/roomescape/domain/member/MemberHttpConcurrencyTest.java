package roomescape.domain.member;

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
public class MemberHttpConcurrencyTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        RestAssured.port = this.port;
    }

    @Test
    void 동일한_정보로_동시에_회원가입_요청하면_하나만_201이고_나머지는_409를_응답한다() throws InterruptedException, ExecutionException {
        // given
        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);

        Map<String, String> params = new HashMap<>();
        params.put("name", "동시요청");
        params.put("email", "concurrency@email.com");
        params.put("password", "concurrency");

        List<Future<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            futures.add(executor.submit(() -> {
                readyLatch.countDown();
                startLatch.await();

                return RestAssured.given()
                        .body(params)
                        .contentType(ContentType.JSON)
                        .when().post("/members")
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
}
