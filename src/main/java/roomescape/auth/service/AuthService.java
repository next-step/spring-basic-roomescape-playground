package roomescape.auth.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.request.LoginRequest;
import roomescape.member.dto.response.LoginCheckResponse;
import roomescape.member.dto.response.LoginResponse;

@Service
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public AuthService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public LoginResponse login(LoginRequest request) {
        validateLoginValues(request);
        Member member = getMemberWithLogin(request);
        String accessToken = jwtTokenProvider.createAccessToken(member);
        return new LoginResponse(accessToken);
    }

    private void validateLoginValues(LoginRequest request) {
        validateEmailNotBlank(request.email());
        validatePasswordNotBlank(request.password());
    }

    private void validateEmailNotBlank(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_EMAIL.getMessage());
        }
    }

    private void validatePasswordNotBlank(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_PASSWORD.getMessage());
        }
    }

    private Member getMemberWithLogin(LoginRequest request) {
        return memberDao.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    public LoginCheckResponse loginCheck(Cookie cookie) {
        validateCookie(cookie);
        String accessToken = cookie.getValue();
        Member member = getLoginMember(accessToken);
        return new LoginCheckResponse(member);
    }

    public Member getLoginMember(String accessToken) {
        long memberId = jwtTokenProvider.parseToken(accessToken);
        return getMemberById(memberId);
    }

    private void validateCookie(Cookie cookie) {
        if (cookie == null
                || cookie.getValue() == null
                || cookie.getValue().isBlank()) {
            throw new BadRequestException(ExceptionMessage.COOKIE_NOT_FOUND.getMessage());
        }
    }

    private Member getMemberById(long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    public boolean isNotAdmin(String accessToken) {
        Member member = getLoginMember(accessToken);
        return member.isNotAdmin();
    }
}
