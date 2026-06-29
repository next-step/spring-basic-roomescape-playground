package roomescape.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.auth.dto.AuthResult;
import roomescape.auth.dto.MemberInfo;
import roomescape.member.MemberRequest;
import roomescape.member.MemberResponse;
import roomescape.member.MemberService;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;

    private final MemberService memberService;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public AuthResult signUp(MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.createMember(memberRequest);
        MemberInfo memberInfo = MemberInfo.from(memberResponse);
        String token = jwtTokenProvider.createToken(memberResponse.id(), memberResponse.name(), memberResponse.role());

        return new AuthResult(token, memberInfo);
    }

    public AuthResult login(MemberRequest memberRequest) {
        MemberResponse memberResponse = memberService.loadMember(memberRequest);
        MemberInfo memberInfo = MemberInfo.from(memberResponse);
        String token = jwtTokenProvider.createToken(memberResponse.id(), memberResponse.name(), memberResponse.role());

        return new AuthResult(token, memberInfo);
    }

    public AuthResult loginCheck(String token) {
        Long id = jwtTokenProvider.validateToken(token);
        MemberResponse memberResponse = memberService.loadMember(id);
        MemberInfo memberInfo = MemberInfo.from(memberResponse);

        return new AuthResult(token, memberInfo);
    }
}
