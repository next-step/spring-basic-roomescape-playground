package roomescape.member.service;

import org.springframework.stereotype.Service;
import roomescape.global.token.JwtTokenProvider;
import roomescape.member.entity.Member;
import roomescape.member.dao.MemberDao;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.entity.Role;

@Service
public class MemberService {
    private MemberDao memberDao;
    private JwtTokenProvider jwtTokenProvider;

    public MemberService(MemberDao memberDao, JwtTokenProvider jwtTokenProvider) {
        this.memberDao = memberDao;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberDao.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findByToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String email = jwtTokenProvider.getPayload(token);

        return memberDao.findByEmail(email);
    }

    public String login(MemberRequest memberRequest) {
        Member member = memberDao.findByEmailAndPassword(
                memberRequest.getEmail(),
                memberRequest.getPassword()
        );

        if (member == null) {
            throw new RuntimeException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }

        String accessToken = jwtTokenProvider.createToken(member.getEmail());

        return accessToken;
    }
}
