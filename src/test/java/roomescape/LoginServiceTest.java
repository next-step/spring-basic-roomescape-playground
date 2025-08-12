package roomescape;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import roomescape.auth.service.LoginService;
import roomescape.auth.util.JwtPayload;
import roomescape.auth.util.JwtUtil;
import roomescape.member.Member;
import roomescape.member.MemberDao;
import roomescape.member.MemberInfo;

public class LoginServiceTest {

    MemberDao memberDao = Mockito.mock(MemberDao.class);
    JwtUtil jwtUtil = Mockito.mock(JwtUtil.class);
    LoginService loginService = new LoginService(memberDao, jwtUtil);

    @Test
    @DisplayName("로그인 호출 시 토큰을 반환한다.")
    void loginReturnsToken() {
        Member member = new Member(1L, "브라운", "brown@email.com", "ADMIN");
        Mockito.when(memberDao.findByEmailAndPassword("brown@email.com", "password"))
            .thenReturn(member);
        Mockito.when(jwtUtil.createToken(member)).thenReturn("tokenValue");

        String token = loginService.login("brown@email.com", "password");
        assertThat(token).isEqualTo("tokenValue");
    }

    @Test
    @DisplayName("check 호출 시 MemberInfo 반환")
    void checkReturnsMemberInfo() {
        JwtPayload payload = new JwtPayload("브라운", "ADMIN");
        Member member = new Member(1L, "브라운", "brown@email.com", "ADMIN");

        Mockito.when(jwtUtil.parseToken("token")).thenReturn(payload);
        Mockito.when(memberDao.findByName("브라운")).thenReturn(member);

        MemberInfo info = loginService.check("token");
        assertThat(info.name()).isEqualTo("브라운");
        assertThat(info.role()).isEqualTo("ADMIN");
    }
}
