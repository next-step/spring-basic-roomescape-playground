package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.global.auth.JwtTokenProvider;
import roomescape.member.controller.dto.MemberLoginRequest;
import roomescape.member.controller.dto.MemberRequest;
import roomescape.member.controller.dto.MemberResponse;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberDao memberDao;
    private final JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(
            new Member(memberRequest.getName(),
                memberRequest.getEmail(),
                memberRequest.getPassword(),
                "USER")
        );

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String login(MemberLoginRequest request) {
        Member member = memberDao.findByEmailAndPassword(
            request.email(),
            request.password()
        ).orElseThrow(() -> new IllegalArgumentException("계정정보가 올바르지 않습니다."));

        return jwtTokenProvider.createToken(member);
    }

    public Member checkLogin(String token) {
        Long memberId = jwtTokenProvider.getMemberId(token);

        return memberDao.findById(memberId)
            .orElseThrow(IllegalArgumentException::new);
    }
}
