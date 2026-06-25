package roomescape.member;

import jakarta.servlet.http.Cookie;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public String createToken(String email, String password) {
        validateLogin(email, password);
        Member member = getMemberByEmailAndPassword(email, password);
        return jwtTokenProvider.createToken(member);
    }

    public Member extractMemberFromCookie(Cookie[] cookies) {
        String token = extractTokenFromCookie(cookies);
        String name = jwtTokenProvider.getTokenPayload(token).get("name", String.class);
        return getMemberByName(name);
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
                new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    private void validateLogin(String email, String password) {
        if (email.isBlank() || password.isBlank()) {
            throw new IllegalArgumentException("이메일과 패스워드를 모두 입력해주세요");
        }
    }

    private Member getMemberByName(String name) {
        try {
            return memberDao.findByName(name);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("이름이 틀렸습니다");
        }
    }

    private Member getMemberByEmailAndPassword(String email, String password) {
        try {
            return memberDao.findByEmailAndPassword(email, password);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("이메일 또는 패스워드가 틀렸습니다");
        }
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }
        throw new IllegalArgumentException("토큰이 필요합니다");
    }
}
