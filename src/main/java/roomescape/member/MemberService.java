package roomescape.member;

import jakarta.persistence.NoResultException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

@Service
@Transactional
public class MemberService {
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        Member member = memberRepository.save(new Member(memberRequest.getName(), memberRequest.getEmail(), memberRequest.getPassword(), "USER"));
        return new MemberResponse(member.getId(), member.getName(), member.getEmail(), member.getRole());
    }

    public Member authenticate(String email, String password) {
        try {
            return memberRepository.getByEmailAndPassword(email, password);
        } catch (NoResultException e) {
            throw new RoomEscapeException(ErrorCode.INVALID_LOGIN);
        }
    }

    public MemberResponse findById(Long memberId) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() ->new RoomEscapeException(ErrorCode.MEMBER_NOT_FOUND));
        return new MemberResponse(findMember.getId(), findMember.getName(), findMember.getEmail(), findMember.getRole());
    }

}
