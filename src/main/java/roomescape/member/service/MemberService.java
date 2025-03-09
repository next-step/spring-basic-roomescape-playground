package roomescape.member.service;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.common.exception.BadRequestException;
import roomescape.common.exception.ExceptionMessage;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.dto.request.LoginRequest;
import roomescape.member.dto.request.MemberRequest;
import roomescape.member.dto.response.LoginCheckResponse;
import roomescape.member.dto.response.LoginResponse;
import roomescape.member.dto.response.MemberResponse;
import roomescape.member.util.JwtTokenProvider;

@Service
public class MemberService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberDao memberDao;

    public MemberService(JwtTokenProvider jwtTokenProvider, MemberDao memberDao) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public LoginResponse login(LoginRequest request) {
        validateLoginValues(request);
        Member member = getMemberWithLogin(request);
        String accessToken = jwtTokenProvider.createToken(member);
        return new LoginResponse(accessToken);
    }

    private void validateLoginValues(LoginRequest request) {
        validateEmailNotBlank(request.email());
        validatePasswordNotBlank(request.password());
    }

    private void validateEmailNotBlank(String email) {
        if (email.isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_EMAIL.getMessage());
        }
    }

    private void validatePasswordNotBlank(String password) {
        if (password.isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_PASSWORD.getMessage());
        }
    }

    private Member getMemberWithLogin(LoginRequest request) {
        return memberDao.findByEmailAndPassword(request.email(), request.password())
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND.getMessage()));
    }

    public LoginCheckResponse loginCheck(Cookie cookie) {
        String accessToken = cookie.getValue();
        long memberId = jwtTokenProvider.parseToken(accessToken);
        Member member = getMemberById(memberId);
        return new LoginCheckResponse(member);
    }

    private Member getMemberById(long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND.getMessage()));
    }
}
