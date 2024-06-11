package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import roomescape.auth.JwtUtils;
import roomescape.provider.CookieProvider;
import roomescape.provider.TokenProvider;

import java.util.Map;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    public MemberService(MemberRepository memberRepository, JwtUtils jwtUtils) {
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;

import roomescape.provider.CookieProvider;
import roomescape.provider.TokenProvider;

@Service
public class MemberService {
    private MemberDao memberDao;
    private TokenProvider tokenProvider;
    private CookieProvider cookieProvder;

    public MemberService(MemberDao memberDao, TokenProvider tokenProvider, CookieProvider cookieProvder) {
        this.memberDao = memberDao;
        this.tokenProvider = tokenProvider;
        this.cookieProvder = cookieProvder;
    }
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(),member.getRole());
    }

    public String login(LoginRequest loginRequest) {
        Member member = memberRepository.findByEmailAndPassword(loginRequest.getEmail(),loginRequest.getPassword());

        return jwtUtils.createToken(member.getId().toString(), Map.of("name", member.getName(), "role", member.getRole()));
    }

    public MemberResponse checkLogin (String token) {
        String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

        Long memberId = Long.valueOf(Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody().getSubject());
        Member member = memberRepository.findById(memberId).orElse(new Member());

        return findById(member.getId());
    }

    public MemberResponse findById(Long id) {
        Member member = memberRepository.findById(id).orElse(new Member());
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}

    public void login(LoginRequest loginRequest, HttpServletResponse httpServletResponse) {
        Member member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if(member == null) throw new IllegalArgumentException();
        String token = tokenProvider.createAccessToken(member);
        Cookie cookie = cookieProvder.createCookie(token);
        httpServletResponse.addCookie(cookie);
    }

    public LoginResponse loginCheck(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token = cookieProvder.extractTokenFromCookie(cookies);
        String memberName = tokenProvider.getMemberFromToken(token);
        Member member = memberDao.findByName(memberName);
        return new LoginResponse(member.getName());
    }
}
