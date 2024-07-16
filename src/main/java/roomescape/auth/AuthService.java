package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.infrastructure.JwtTokenUtil;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.token.TokenRequest;
import roomescape.token.TokenResponse;

@Service
public class AuthService {
    private final String INVALID_MEMBER_MSG = "존재하지 않는 email 또는 password 입니다.";

    private final JwtTokenUtil jwtTokenUtil;
    private final MemberRepository memberRepository;

    public AuthService(JwtTokenUtil jwtTokenUtil, MemberRepository memberRepository) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.memberRepository = memberRepository;
    }

    public Member checkInvalidLogin(String principal, String credentials) {
        Member member = memberRepository.findByEmailAndPassword(principal, credentials);
        if(member == null) {
            throw new AuthorizationException(INVALID_MEMBER_MSG);
        }

        return member;
    }

    public Long findMemberIdByToken(Cookie[] cookies) {
        return jwtTokenUtil.getPayload(cookies);
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        Member member = checkInvalidLogin(tokenRequest.getEmail(), tokenRequest.getPassword());
        String accessToken = jwtTokenUtil.createToken(member);
        return new TokenResponse(accessToken);
    }
}
