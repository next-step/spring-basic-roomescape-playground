package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import auth.JwtUtils;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberDao) {
        this.memberRepository = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(),member.getRole());
    }

    public Member findMember(String email, String password){
        Member member= memberRepository.findByEmailAndPassword(email,password);
        return member;
    }

    public Member findById(Long memberId){
        Member member = memberRepository.findById(memberId).orElse(null);
        return member;

    }

}
