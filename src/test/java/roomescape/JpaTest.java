package roomescape;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class JpaTest {

    @Value("${roomescape.auth.jwt.secret}")
    private String secretKey;

    @Test
    void 팔단계() {
        // 비밀키 프로퍼티 값을 성공적으로 불러왔는지 확인 (빈 값이 아니어야 성공)
        assertThat(secretKey).isNotBlank();
    }
}
