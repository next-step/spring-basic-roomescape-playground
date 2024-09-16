package roomescape.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.global.auth.JwtProvider;
import roomescape.global.auth.LoginRequest;
import roomescape.global.exception.AuthenticationException;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberDao memberDao;
    private final JwtProvider jwtProvider;

    @Autowired
    public MemberService(MemberDao memberDao, JwtProvider jwtProvider) {
        this.memberDao = memberDao;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String memberLogin(LoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(request.email(), request.password());
        if(member == null) {
            throw new AuthenticationException("로그인 정보가 존재하지 않습니다.");
        }

        return jwtProvider.createToken(member);
    }

    public Member getAuth(String token) {
        Long userId = jwtProvider.getUserId(token);
        return memberDao.findById(userId).orElseThrow(AuthenticationException::new);
    }
}
