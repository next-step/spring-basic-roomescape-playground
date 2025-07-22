package auth;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.Member;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class JwtUtilsTest {

    private static final String SECRET_KEY = "a-very-long-and-secure-secret-key-for-testing-purposes-only";
    private static final String ISSUER = "test-issuer";
    private static final int ITERATIONS = 10000; // 반복 횟수 줄임
    private static final int DURATION_MS = 2000; // 2초

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member(1L, "tester", "test@test.com", "USER");
    }

    private void runPerformanceTest(String version, Runnable testLogic) {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            testLogic.run();
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("[%s] Latency Test: %d ms%n", version, (endTime - startTime));
    }

    private void runThroughputTest(String version, Runnable testLogic) {
        long startTime = System.currentTimeMillis();
        long operations = 0;
        while (System.currentTimeMillis() - startTime < DURATION_MS) {
            testLogic.run();
            operations++;
        }
        System.out.printf("[%s] Throughput Test: %.2f ops/sec%n", version, operations / (DURATION_MS / 1000.0));
    }

    private void runMultiThreadTest(String version, Runnable testLogic) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(8);
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            executor.submit(testLogic);
        }
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        long endTime = System.currentTimeMillis();
        System.out.printf("[%s] Multi-Thread Test: %d ms%n", version, (endTime - startTime));
    }

    // --- V1 Tests ---
    @Test
    @DisplayName("V1 성능 프로파일링")
    void performanceProfileV1() throws InterruptedException {
        JwtUtils jwtUtils = new JwtUtils(SECRET_KEY, ISSUER);
        String token = jwtUtils.generateToken(testMember);
        Runnable logic = () -> {
            jwtUtils.getMemberIdByToken(token);
            jwtUtils.getRoleByToken(token);
        };
        runPerformanceTest("V1", logic);
        runThroughputTest("V1", logic);
        runMultiThreadTest("V1", logic);
    }

    // --- V2 Tests ---
    @Test
    @DisplayName("V2 성능 프로파일링")
    void performanceProfileV2() throws InterruptedException {
        JwtUtilsV2 jwtUtils = new JwtUtilsV2(SECRET_KEY, ISSUER);
        String token = jwtUtils.generateToken(testMember);
        Runnable logic = () -> {
            jwtUtils.getMemberIdByToken(token);
            jwtUtils.getRoleByToken(token);
        };
        runPerformanceTest("V2", logic);
        runThroughputTest("V2", logic);
        runMultiThreadTest("V2", logic);
    }

    // --- V3 Tests ---
    @Test
    @DisplayName("V3 성능 프로파일링")
    void performanceProfileV3() throws InterruptedException {
        JwtUtilsV3 jwtUtils = new JwtUtilsV3(SECRET_KEY, ISSUER);
        String token = jwtUtils.generateToken(testMember);
        Runnable logic = () -> {
            jwtUtils.getMemberIdByToken(token);
            jwtUtils.getRoleByToken(token);
        };
        runPerformanceTest("V3", logic);
        runThroughputTest("V3", logic);
        runMultiThreadTest("V3", logic);
    }

    // --- V4 Tests ---
    @Test
    @DisplayName("V4 성능 프로파일링")
    void performanceProfileV4() throws InterruptedException {
        JwtUtilsV4 jwtUtils = new JwtUtilsV4(SECRET_KEY, ISSUER);
        String token = jwtUtils.generateToken(testMember);
        Runnable logic = () -> {
            Claims claims = jwtUtils.getClaims(token);
            Long.valueOf(claims.getSubject());
            claims.get("role", String.class);
        };
        runPerformanceTest("V4", logic);
        runThroughputTest("V4", logic);
        runMultiThreadTest("V4", logic);
    }
}
