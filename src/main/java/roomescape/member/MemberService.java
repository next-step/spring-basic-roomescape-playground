package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.exception.AuthenticationException;

@Service
public class MemberService {
    private MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest request) {
        try {
            Member member = memberDao.findByEmailAndPassword(request.getEmail(), request.getPassword());
            return jwtTokenProvider.createToken(member);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthenticationException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    public Member findMemberByToken(String token) {
        long id = jwtTokenProvider.getMemberId(token);
        return memberDao.findById(id);
    }
}
