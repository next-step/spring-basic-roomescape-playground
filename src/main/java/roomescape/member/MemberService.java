package roomescape.member;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.common.exception.BadRequestException;
import roomescape.common.exception.ExceptionMessage;
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
        if (request.email().isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_EMAIL.getMessage());
        }
        if (request.password().isBlank()) {
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
        return new LoginCheckResponse(member.getName());
    }

    private Member getMemberById(long memberId) {
        return memberDao.findById(memberId)
                .orElseThrow(() -> new BadRequestException(ExceptionMessage.MEMBER_NOT_FOUND.getMessage()));
    }
}
