package roomescape.member.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import roomescape.global.auth.JwtUtils;
import roomescape.global.auth.LoginRequest;
import roomescape.global.exception.AuthenticationException;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.repository.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;
    private final JwtUtils jwtUtils;

    @Autowired
    public MemberService(MemberRepository memberRepository, JwtUtils jwtUtils) {
        this.memberRepository = memberRepository;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public String memberLogin(LoginRequest request) {
        Member member = memberRepository.findByEmailAndPassword(request.email(), request.password());
        if(member == null) {
            throw new AuthenticationException("로그인 정보가 존재하지 않습니다.");
        }

        return jwtUtils.createToken(member);
    }

    public Member getAuth(String token) {
        Long memberId = jwtUtils.getMemberId(token);
        return memberRepository.findById(memberId).orElseThrow(AuthenticationException::new);
    }
}
