package roomescape.member;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.jwt.JwtProvider;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtProvider jwtProvider;

    public MemberService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
            new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public MemberResponse findMemberByName(String name) {
        Member member = memberDao.findByName(name);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public void login(
        MemberLoginRequest memberLoginRequest,
        HttpServletResponse response
    ) {
        String email = memberLoginRequest.getEmail();
        String password = memberLoginRequest.getPassword();

        String accessToken = jwtProvider.createToken(email, password);

        Cookie cookie = new Cookie("token", accessToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public MemberLoginCheck loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        String name = jwtProvider.getNameFromToken(token);

        return new MemberLoginCheck(name);
    }

    private String extractTokenFromCookie(Cookie[] cookies) {
        return Arrays.stream(cookies)
            .filter(cookie -> cookie.getName().equals("token"))
            .findFirst()
            .map(Cookie::getValue)
            .orElse("");
    }
}
