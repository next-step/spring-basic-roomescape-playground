package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.global.util.JwtTokenProvider;

@Service
public class LoginService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public LoginService(MemberDao  memberDao, JwtTokenProvider jwtTokenProvider) {
        this. memberDao =  memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(String email, String password) {
        Member member =  memberDao.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다."));
        return jwtTokenProvider.createToken(member);
    }

    public Member getMemberFromToken(String token) {
        Long memberId = jwtTokenProvider.getMemberId(token);
        return memberDao.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
    }
}
