package roomescape.member;

import org.springframework.stereotype.Service;
import roomescape.api.JwtDecoder;
import roomescape.api.JwtProvider;
import roomescape.exception.NotFoundException;
import roomescape.member.dto.MemberRequest;
import roomescape.member.dto.MemberResponse;

@Service
public class MemberService {
    private MemberRepository memberRepository;
    private JwtProvider jwtProvider;

    public MemberService(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    public MemberResponse.Create createMember(MemberRequest.Create memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse.Create(member.getId(), member.getName(), member.getEmail());
    }

    public String loginMember(MemberRequest.Login request) {
        Member member = memberRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if(member == null){
            throw new NotFoundException("유저를 찾을 수 없습니다.");
        }
        return jwtProvider.createToken(member);
    }

    public MemberResponse.Check checkMember(String token) {
        Long memberId = JwtDecoder.decodeJwtToken(token);
        Member member =  memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("유저가 없습니다."));

        return MemberResponse.Check.from(member);
    }

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("유저가 없습니다."));
    }

    public Member findByEmailAndPassword(String email, String password) {
        Member member = memberRepository.findByEmailAndPassword(email, password);
        if(member == null ){
            throw new NotFoundException("유저를 찾을 수 없습니다.");
        }
        return member;
    }
}
