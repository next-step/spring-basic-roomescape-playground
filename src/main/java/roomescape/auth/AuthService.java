package roomescape.auth;

import io.jsonwebtoken.Claims;
import auth.JwtService;
import org.springframework.stereotype.Service;
import roomescape.exception.UnauthorizedException;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Service
public class AuthService {

    private final MemberRepository memberRepo;
    private final JwtService jwtService;

    public AuthService(MemberRepository memberRepo, JwtService jwtService) {
        this.memberRepo = memberRepo;
        this.jwtService = jwtService;
    }

    public String loginToken(String email, String password) {
        Member member = memberRepo.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다."));
        return jwtService.createToken(member);
    }

    public Member checkLogin(String token) {
        String email = jwtService.getEmail(token);
        return memberRepo.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("존재하지 않는 회원입니다."));
    }

    public void authorizeAdmin(String token) {
        String role = jwtService.getRole(token);
        if (!"ADMIN".equals(role)) {
            throw new UnauthorizedException("관리자 권한이 필요합니다.");
        }
    }
}
