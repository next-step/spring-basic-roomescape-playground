package roomescape;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.auth.JwtTokenProvider;
import roomescape.member.Member;
import roomescape.member.MemberRequest;
import roomescape.member.MemberService;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @Test
    @DisplayName("로그인 성공 테스트")
    void login_success() throws Exception {
        // Given
        Map<String, String> requestMap = Map.of(
                "email", "test@example.com",
                "password", "password",
                "name", "Test User"
        );
        Member member = new Member(1L, "test@example.com", "password", "Test User");
        String token = "test-jwt-token";

        // Mock 설정
        given(memberService.login("test@example.com", "password")).willReturn(member);
        given(jwtTokenProvider.createToken(member.getEmail())).willReturn(token);

        // When & Then
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMap)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful : Test User"))
                .andExpect(cookie().value("token", token))
                .andExpect(cookie().httpOnly("token", true))
                .andExpect(cookie().maxAge("token", 3600));
    }
}
