package roomescape.auth.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenManager;
import roomescape.auth.dto.LoginMember;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.request.LoginRequest;
import roomescape.member.dto.response.LoginCheckResponse;
import roomescape.member.dto.response.LoginResponse;

@Service
public class AuthService {

    private final JwtTokenManager jwtTokenManager;
    private final MemberDao memberDao;

    public AuthService(JwtTokenManager jwtTokenManager, MemberDao memberDao) {
        this.jwtTokenManager = jwtTokenManager;
        this.memberDao = memberDao;
    }

    public LoginResponse login(LoginRequest request) {
        Member member = getMemberWithLogin(request);
        String accessToken = jwtTokenManager.createAccessToken(member);
        return new LoginResponse(accessToken);
    }

    private Member getMemberWithLogin(LoginRequest request) {
        return memberDao.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    public LoginCheckResponse loginCheck(LoginMember loginMember) {
        return new LoginCheckResponse(loginMember.name());
    }

    public Member getLoginMember(String accessToken) {
        long memberId = jwtTokenManager.parseToken(accessToken);
        return getMemberById(memberId);
    }

    private Member getMemberById(long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND.getMessage()));
    }
}
