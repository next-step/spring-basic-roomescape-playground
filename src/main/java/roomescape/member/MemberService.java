package roomescape.member;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.global.auth.JwtService;
import roomescape.member.dto.LoginMember;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.exception.MemberNotFoundException;

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
        if (member == null) throw new MemberNotFoundException();


        return jwtService.generateToken(member);
    }

    public LoginMember checkLogin(Cookie[] cookies) {
        String token = jwtService.extractTokenFromCookie(cookies);
        Long userId = jwtService.decodeToken(token);

        Optional<Member> memberOptional = memberRepository.findById(userId);
        if (memberOptional.isEmpty()) throw new MemberNotFoundException();

        Member member = memberOptional.get();
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }
}
