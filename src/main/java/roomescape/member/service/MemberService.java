package roomescape.member.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Service;
import roomescape.member.controller.dto.MemberRequest;
import roomescape.member.controller.dto.MemberResponse;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberDao;

@Service
public class MemberService {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private final MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(requestToMember(memberRequest));
        return memberToResponse(member);
    }

    private Member requestToMember(MemberRequest memberRequest) {
        return new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER");
    }

    private MemberResponse memberToResponse(Member member) {
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(memberRequest.email(), memberRequest.password());
        if (member == null) {
            throw new RuntimeException("로그인 실패");
        }

        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public MemberResponse getMemberByName(String memberName) {
        if (memberName == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        return memberDao.findByName(memberName)
                .map(this::memberToResponse)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
    }
}
