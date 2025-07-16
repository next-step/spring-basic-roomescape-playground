package roomescape.auth;

import jakarta.servlet.http.Cookie;
import jwt.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminAuthorizationInterceptorTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void user_가_admin_페이지_접근하면_예외를_던진다() throws Exception {

        Member member = memberRepository.findByEmail("brown@email.com").get();

        String token = jwtProvider.createToken(member);
        mockMvc.perform(get("/admin")
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value("UNAUTHORIZED_ACCESS"))
                .andExpect(jsonPath("$.message").value("관리자 권한이 필요합니다."));
    }

    @Test
    void admin_이면_정상_흐름() throws Exception {

        Member member = memberRepository.findByEmail("admin@email.com").get();

        String token = jwtProvider.createToken(member);
        mockMvc.perform(get("/admin")
                        .cookie(new Cookie("token", token)))
                .andExpect(status().isOk());
    }
}
