package roomescape.auth;

import org.springframework.stereotype.Service;
import roomescape.auth.DTO.LoginRequest;
import roomescape.auth.config.TokenProvider;
import roomescape.member.DTO.MemberResponse;
import roomescape.member.MemberService;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    public AuthService(MemberService memberService, TokenProvider tokenProvider) {
        this.memberService = memberService;
        this.tokenProvider = tokenProvider;
    }

    public String login(LoginRequest request) {
        MemberResponse response = memberService.findByEmailAndPassword(request);
        //TODO: 만약 없으면 인증안된거고, 나오면 토큰 발급하자
        if (response == null) {
            throw new IllegalArgumentException("일치하는 회원 정보가 없어요!");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("name", response.getName());
        return tokenProvider.createToken(response.getEmail(), claims);
    }

    public void logout() {
    }
}
