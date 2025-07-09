package roomescape.member;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse createMember(MemberRequest memberRequest) {
        validateDuplicateEmail(memberRequest);
        Member member = memberRepository.save(memberRequest.toEntity());
        return MemberResponse.from(member);
    }

    public Member authenticate(String email, String password) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RoomEscapeException(ErrorCode.INVALID_LOGIN, "이메일이 잘못되었습니다."));
        if (!findMember.getPassword().equals(password)) {
            throw new RoomEscapeException(ErrorCode.INVALID_LOGIN, "비밀번호가 틀렸습니다.");
        }
        return findMember;
    }

    public MemberResponse getById(Long memberId) {
        Member findMember = memberRepository
                .findById(memberId).orElseThrow(() -> new RoomEscapeException(ErrorCode.MEMBER_NOT_FOUND));
        return MemberResponse.from(findMember);
    }

    private void validateDuplicateEmail(MemberRequest memberRequest) {
        if (memberRepository.existsByEmail(memberRequest.email())) {
            throw new RoomEscapeException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

}
