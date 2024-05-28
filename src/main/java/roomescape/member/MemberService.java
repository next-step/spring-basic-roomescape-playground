package roomescape.member;

import auth.JwtUtils;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String createToken(LoginRequest loginRequest) {
        if (!checkValidLogin(loginRequest.getEmail(), loginRequest.getPassword())) {
            throw new AuthorizationException();
        }

        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

        return jwtUtils.createToken(member.getId(), member.getName(), member.getRole());
    }

    public Cookie createCookie(String accessToken) {
        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return cookie;
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        throw new AuthorizationException();
    }

    public Member extractMemberFromToken(String token) {
        Long memberId = jwtUtils.extractIdFromToken(token);

        return memberRepository.findById(memberId).get();
    }

    public boolean checkValidLogin(String principal, String credentials) {
        return memberRepository.existsByEmailAndPassword(principal, credentials);
    }
}
