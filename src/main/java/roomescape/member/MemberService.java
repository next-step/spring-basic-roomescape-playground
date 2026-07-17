package roomescape.member;

import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberDao) {
        this.memberRepository = memberDao;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member =
                new Member(
                        memberRequest.getName(),
                        memberRequest.getEmail(),
                        memberRequest.getPassword(),
                        "USER");

        memberRepository.save(member);
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
