package roomescape.member.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import auth.JwtProvider;
import roomescape.member.dto.MemberLoginRequest;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;
import roomescape.member.model.Member;
import roomescape.member.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public MemberResponse createMember(MemberRequest request) {
        Member member = memberRepository.save(
            Member.builder()
                .name(request.name())
                .email(request.email())
                .password(request.password())
                .role("USER")
                .build()
        );
        return MemberResponse.from(member);
    }

    public String memberLogin(MemberLoginRequest request) {
        Member member = memberRepository.getByEmailAndPassword(request.email(), request.password());
        return jwtProvider.createToken(member);
    }
}
