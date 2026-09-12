package roomescape.domain.member.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberDao;
import roomescape.global.auth.jwt.JwtTokenProvider;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public String login(String email, String password) {
        Member foundMember = memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호가 잘못되었습니다."));

        return jwtTokenProvider.createToken(foundMember);
    }

    public String getUsername(String token) {
        return jwtTokenProvider.getName(token);
    }
}
