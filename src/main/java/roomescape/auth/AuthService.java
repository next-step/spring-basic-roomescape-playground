package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.auth.jwt.MemberTokenDto;
import roomescape.auth.jwt.TokenService;
import roomescape.member.Member;
import roomescape.member.MemberDao;

@Service
public class AuthService {

    private final MemberDao memberDao;
    private final TokenService tokenService;

    public AuthService(MemberDao memberDao, TokenService tokenService) {
        this.memberDao = memberDao;
        this.tokenService = tokenService;
    }

    public String loginWithEmailAndPassword(String email, String password) {
        Member member = memberDao.findByEmailAndPassword(email, password);

        if(member == null) {
            throw new IllegalArgumentException("잘못된 이메일 또는 비밀번호입니다.");
        }

        return tokenService.createToken(
                new MemberTokenDto(member.getId(), member.getName(), member.getEmail(), member.getRole()));
    }

    public MemberDetailResponse loginCheckWithToken(String token) {
        //유효기간 확인을 위해 필요
        if (!tokenService.checkValidToken(token)) {
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }

        MemberTokenDto member = tokenService.getMemberClaims(token);
        return new MemberDetailResponse(member.id(), member.name(), member.email(), member.role());
    }
}
