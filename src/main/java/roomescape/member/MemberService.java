package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.AuthenticationException;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.LoginMemberInfo;
import roomescape.auth.LoginTokens;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), MemberRole.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public LoginTokens login(MemberRequest memberRequest) {
        try {
            Member member = memberDao.findByEmailAndPassword(memberRequest.getEmail(), memberRequest.getPassword());
            return new LoginTokens(
                    jwtTokenProvider.createAccessToken(member),
                    jwtTokenProvider.createRefreshToken(member)
            );
        } catch (EmptyResultDataAccessException e) {
            throw new AuthenticationException();
        }
    }

    public LoginMemberInfo checkLogin(String token) {
        return jwtTokenProvider.parseAccessToken(token);
    }

    public String refreshAccessToken(String refreshToken) {
        try {
            LoginMemberInfo loginMember = jwtTokenProvider.parseRefreshToken(refreshToken);
            Member member = memberDao.findByEmail(loginMember.email());
            return jwtTokenProvider.createAccessToken(member);
        } catch (EmptyResultDataAccessException | IllegalArgumentException e) {
            throw new AuthenticationException();
        }
    }
}
