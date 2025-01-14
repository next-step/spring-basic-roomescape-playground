package roomescape.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class JwtTokenProviderTest {
    private static final String EMAIL = "email@email.com";

    @Autowired
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider();


    @Test
    void createToken() {
        String token = jwtTokenProvider.createToken(EMAIL);
        assertThat(token).isNotBlank();
    }

}
