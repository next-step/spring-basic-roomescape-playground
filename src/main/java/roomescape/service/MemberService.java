package roomescape.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.repository.MemberRepository;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.exception.UnauthorizedException;
import roomescape.model.Member;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse create(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), "USER"));

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findById(String id) {
        return memberRepository.findById(Long.parseLong(id)).orElseThrow(() -> new UnauthorizedException("유효하지 않은 토큰입니다."));
    }

    public Member authenticate(String email, String password) {
        return memberRepository.findByEmailAndPassword(email, password).orElseThrow(() -> new UnauthorizedException("유효한 인증 정보가 없습니다."));
    }
}
