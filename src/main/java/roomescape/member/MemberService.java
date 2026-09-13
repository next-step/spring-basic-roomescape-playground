package roomescape.member;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.TokenUtil;

@Service
public class MemberService {
    private MemberDao memberDao;
    private TokenUtil tokenUtil;

    public MemberService(MemberDao memberDao, TokenUtil tokenUtil) {
        this.memberDao = memberDao;
        this.tokenUtil = tokenUtil;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(LoginRequest loginRequest) {
        Member member;
        try {
            member = memberDao.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorizationException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return tokenUtil.createToken(member);
    }

    public Member findMemberByToken(String token) {
        Long memberId = tokenUtil.getMemberId(token);

        try {
            return memberDao.findById(memberId);
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorizationException("존재하지 않는 회원입니다.");
        }
    }
}
