package roomescape.member;

import static roomescape.auth.CookiesUtils.extractTokenFromCookie;
import static roomescape.auth.CookiesUtils.setTokenToCookie;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import roomescape.auth.JwtProvider;

@Service
public class MemberService {

    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

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
        MemberRequest memberRequest,
        HttpServletResponse response
    ) {
        String email = memberRequest.getEmail();
        String password = memberRequest.getPassword();
        Member member = memberDao.findByEmailAndPassword(email, password);
        if (member == null)
            throw new IllegalArgumentException("Invalid email or password");

        String token = jwtProvider.createToken(member);
        setTokenToCookie(response, token);
    }

    public MemberResponse loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = extractTokenFromCookie(cookies);

        Long id = jwtProvider.getIdFromToken(token);
        Member member = memberDao.findById(id);

        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
