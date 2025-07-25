package roomescape;

import auth.JwtService;
import org.springframework.boot.test.context.TestComponent;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@TestComponent
public class JwtTestHelper {

    private final MemberRepository memberRepo;
    private final JwtService jwtService;

    public JwtTestHelper(MemberRepository memberRepo, JwtService jwtService) {
        this.memberRepo = memberRepo;
        this.jwtService = jwtService;
    }

    public String createToken(String email, String password) {
        Member member = memberRepo.findByEmailAndPassword(email, password)
                .orElseThrow(() ->  new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다."));
        return jwtService.createToken(member);
    }
}
