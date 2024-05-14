package roomescape.member;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.global.auth.JwtService;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

import java.util.Optional;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private JwtService jwtService;

    public MemberService(MemberRepository memberRepository, JwtService jwtService) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }


    public String login(MemberRequest memberRequest) {
        Member member = memberRepository.findByEmailAndPassword(memberRequest.getEmail(), memberRequest.getPassword());

        return jwtService.generateToken(member);
    }

    public LoginMember checkLogin(Cookie[] cookies) {
        String token = jwtService.extractTokenFromCookie(cookies);
        Long userId = jwtService.decodeToken(token);

        Optional<Member> memberOptional = memberRepository.findById(userId);
        if (memberOptional.isEmpty()) return null;

        Member member = memberOptional.get();
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
