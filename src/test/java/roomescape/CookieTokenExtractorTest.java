package roomescape;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import roomescape.auth.CookieTokenExtractor;

import static org.assertj.core.api.Assertions.assertThat;

class CookieTokenExtractorTest {
    private final CookieTokenExtractor extractor = new CookieTokenExtractor();

    @Test
    void extractsTokenAmongOtherCookies() {
        var request = new MockHttpServletRequest();
        request.setCookies(new Cookie("other", "value"), new Cookie("token", "jwt"));
        assertThat(extractor.extract(request)).isEqualTo("jwt");
    }

    @Test
    void missingTokenProducesEmptyValue() {
        var request = new MockHttpServletRequest();
        assertThat(extractor.extract(request)).isEmpty();
        request.setCookies(new Cookie("other", "value"));
        assertThat(extractor.extract(request)).isEmpty();
    }
}
