package roomescape.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.auth.Role;
import roomescape.repository.MemberRepository;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;
import roomescape.exception.UnauthorizedException;
import roomescape.model.Member;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public MemberResponse create(MemberRequest memberRequest) {
        String encodedPassword = passwordEncoder.encode(memberRequest.password());
        Member member = memberRepository.save(new Member(memberRequest.name(), memberRequest.email(), encodedPassword, Role.USER));

        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }

    public Member findById(String id) {
        return memberRepository.findById(Long.parseLong(id)).orElseThrow(() -> new UnauthorizedException("유효하지 않은 토큰입니다."));
    }

    public Member authenticate(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("유효한 인증 정보가 없습니다."));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UnauthorizedException("유효한 인증 정보가 없습니다.");
        }

        return member;
    }
}
