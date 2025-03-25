package roomescape.member;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomescapeNotFoundException;
import roomescape.global.exception.RoomescapeUnauthorizedException;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(
                new Member(memberRequest.name(), memberRequest.email(), memberRequest.password(), Role.USER));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail());
    }
}
