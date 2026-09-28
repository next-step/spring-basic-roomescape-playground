package roomescape;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.dao.DataAccessResourceFailureException;
import roomescape.member.MemberRequest;
import roomescape.member.MemberService;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.datasource.url=jdbc:h2:mem:exception-handling-test")
class ExceptionHandlingTest {
    @Autowired
    private TestRestTemplate client;

    @MockBean
    private MemberService memberService;

    @Test
    void databaseFailureIsNotReportedAsBadRequest() {
        when(memberService.createMember(any(MemberRequest.class)))
                .thenThrow(new DataAccessResourceFailureException("database unavailable"));

        var response = client.postForEntity("/members", Map.of(
                "name", "회원", "email", "member@email.com", "password", "password"
        ), String.class);

        assertThat(response.getStatusCode().value()).isEqualTo(500);
    }
}
