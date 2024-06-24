package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import roomescape.auth.JwtTokenProvider;

@Service
public class MemberService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }


    public MemberResponse findMember(String email, String password){
        Member member = memberDao.findByEmailAndPassword(email,password);
        return new MemberResponse(member.getId(),member.getName(),member.getEmail());
    }

    public MemberResponse findMemberById(Long memberId){
        Member member = memberDao.findById(memberId);
        return new MemberResponse(member.getId(),member.getName(),member.getEmail());
    }

    public String createToken(MemberResponse memberResponse){
        String accessToken = jwtTokenProvider.createToken(memberResponse);
        return accessToken;
    }

    public String extractTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                return cookie.getValue();
            }
        }

        return "";
    }
}