package roomescape.member;

import io.jsonwebtoken.JwtException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(
                memberRequest.name(),
                memberRequest.email(),
                memberRequest.password(),
                "USER"
        ));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest loginRequest) {
        try {
            Member member = memberDao.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
            return jwtTokenProvider.createToken(member.getId());
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("이메일 또는 비밀번호가 올바르지 않습니다.", exception);
        }
    }

    public LoginMember findLoginMemberByToken(String token) {
        try {
            Member member = memberDao.findById(jwtTokenProvider.extractMemberId(token));
            return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getRole());
        } catch (JwtException | IllegalArgumentException | EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("유효하지 않은 로그인 토큰입니다.", exception);
        }
    }

    public Member findById(Long id) {
        return memberDao.findById(id);
    }

    public Member findByName(String name) {
        return memberDao.findByName(name);
    }
}
