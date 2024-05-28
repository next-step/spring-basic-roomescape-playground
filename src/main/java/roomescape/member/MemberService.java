package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.auth.JwtUtils;

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
        return jwtUtils.createCookie(accessToken);
    }


    public String extractTokenFromCookie(Cookie[] cookies) {
        return jwtUtils.extractTokenFromCookie(cookies);
    }
    //extractTokenFromCookie 메서드는 요청의 쿠키 배열에서 token이라는 이름의 쿠키를 찾아 해당 값을 반환
//토큰 쿠키가 없으면 예외


    public Member extractMemberFromToken(String token) {
        Long memberId = jwtUtils.extractMemberIdFromToken(token);
        return memberRepository.findById(memberId).get();
    }
    //extractMemberFromToken 메서드는 JWT토크에서 멤버 ID를 추출하고, 이를 통해 Member객체를 반환
//토큰을 파싱하고, 서명 키를 사용하여 유효성을 검사한 후, subject 클레임에서 멤버 ID를 추출합니다.
//추출된 멤버 ID로 데이터베이스에서 멤버를 조회하여 반환

    public String extractNameFromToken(String token) {
        return jwtUtils.extractNameFromToken(token);
    }

    public boolean checkValidLogin(String principal, String credentials) {
        return memberRepository.existsByEmailAndPassword(principal, credentials);
    }
    //checkValidLogin 메서드는 입력된 이메일과 비밀번호가 유효한지 여부 확인
    //MemberRepository를 사용하여 해당 이메일과 비밀번호로 멤버가 존재하는지 검사
    //존재하면 true, 존재하지 않으면 false
}
