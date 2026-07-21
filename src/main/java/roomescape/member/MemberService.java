package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.AuthenticationException;
import roomescape.auth.JwtTokenProvider;
import roomescape.auth.LoginMemberInfo;
import roomescape.auth.LoginTokens;
import roomescape.exception.ErrorCode;
import roomescape.exception.NotFoundException;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), MemberRole.USER));
        return new MemberResponse(member.id(), member.name(), member.email());
    }

    public LoginTokens login(MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(memberRequest.getEmail(), memberRequest.getPassword())
                .orElseThrow(AuthenticationException::new);

        return new LoginTokens(
                jwtTokenProvider.createAccessToken(member),
                jwtTokenProvider.createRefreshToken(member)
        );
    }

    public LoginMemberInfo checkLogin(String token) {
        try {
            return jwtTokenProvider.parseAccessToken(token);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException();
        }
    }

    public String refreshAccessToken(String refreshToken) {
        try {
            LoginMemberInfo loginMember = jwtTokenProvider.parseRefreshToken(refreshToken);
            Member member = memberDao.findByEmail(loginMember.email())
                    .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));
            return jwtTokenProvider.createAccessToken(member);
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException();
        }
    }
}
