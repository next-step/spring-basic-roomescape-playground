package roomescape.auth;

import io.jsonwebtoken.JwtException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.exception.InvalidAuthenticationException;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            MemberDao memberDao,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String login(LoginRequest loginRequest) {
        try {
            Member member = memberDao.findByEmailAndPassword(
                    loginRequest.email(),
                    loginRequest.password()
            );

            return jwtTokenProvider.createToken(member);
        } catch (EmptyResultDataAccessException exception) {
            throw new InvalidAuthenticationException();
        }
    }

    public LoginMember findMemberByToken(String token) {
        try {
            Long memberId = jwtTokenProvider.extractMemberId(token);
            Member member = memberDao.findById(memberId);

            return new LoginMember(member.getName(), member.getRole());
        } catch (JwtException exception) {
            throw new InvalidAuthenticationException();
        }
    }
}
