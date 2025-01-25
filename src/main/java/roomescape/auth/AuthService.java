package roomescape.auth;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.auth.jwt.MemberTokenDto;
import roomescape.auth.jwt.TokenService;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {

    public static final String WRONG_PASSWORD_EXCEPTION_MESSAGE = "잘못된 비밀번호입니다.";
    public static final String INVALID_EMAIL_EXCEPTION_MESSAGE = "없는 이메일 입니다.";
    public static final String INVALID_TOKEN_EXCEPTION_MESSAGE = "잘못된 토큰입니다.";
    private final MemberDao memberDao;
    private final TokenService tokenService;


    public AuthService(MemberDao memberDao, TokenService tokenService) {
        this.memberDao = memberDao;
        this.tokenService = tokenService;
    }

    public String loginWithEmailAndPassword(String email, String password) {

        Member member = null;
        try {
            checkPasswordByEmail(email, password);
            member = memberDao.findByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException(INVALID_EMAIL_EXCEPTION_MESSAGE, e);
        }

        return tokenService.createToken(
                new MemberTokenDto(member.getId(), member.getName(), member.getEmail(), member.getRole()));
    }

    public MemberDetailResponse loginCheckWithToken(String token) {
        //유효기간 확인을 위해 필요
        if (!tokenService.checkValidToken(token)) {
            throw new IllegalArgumentException(INVALID_TOKEN_EXCEPTION_MESSAGE);
        }

        MemberTokenDto member = tokenService.getMemberClaims(token);
        return new MemberDetailResponse(member.id(), member.name(), member.email(), member.role());
    }

    private void checkPasswordByEmail(String email, String password) {
        String findPassword = memberDao.findPasswordByEmail(email);
        if (!findPassword.equals(password)) {
            throw new IllegalArgumentException(WRONG_PASSWORD_EXCEPTION_MESSAGE);
        }
    }
}
