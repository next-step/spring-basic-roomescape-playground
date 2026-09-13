package roomescape.member;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private MemberDao memberDao;

    public MemberService(MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest loginRequest) {
        Member member;
        try {
            member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorizationException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return Jwts.builder()
                .setSubject(member.getId().toString())
                .claim("name", member.getName())
                .claim("role", member.getRole())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
    }

    public Member findMemberByToken(String token) {
        Long memberId;
        try {
            memberId = Long.valueOf(Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject());
        } catch (RuntimeException e) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }

        try {
            return memberDao.findById(memberId);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorizationException("존재하지 않는 회원입니다.");
        }
    }
}
