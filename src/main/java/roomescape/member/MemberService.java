package roomescape.member;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.global.auth.JwtService;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtService jwtService;

    public MemberService(MemberDao memberDao, JwtService jwtService) {
        this.memberDao = memberDao;
        this.jwtService = jwtService;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }


    public String login(MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(memberRequest.getEmail(), memberRequest.getPassword());
        return jwtService.generateToken(member);
    }

    public LoginMember checkLogin(Cookie[] cookies) {
        String token = jwtService.extractTokenFromCookie(cookies);
        Long userId = jwtService.decodeToken(token);
        if (userId == null) return null;

        Member member = memberDao.findById(userId);
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
