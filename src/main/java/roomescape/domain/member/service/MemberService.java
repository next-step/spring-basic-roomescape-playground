package roomescape.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberDao;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.global.exception.ConflictException;
import roomescape.global.exception.NotFoundException;

import java.util.Map;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member createMember(String name, String email, String password) {

        if (memberRepository.existsByEmail(email)) {
            throw new ConflictException(null, Map.of("email", email), "이미 가입된 이메일입니다.");
        }

        return memberRepository.save(new Member(name, email, password, "USER"));
    }
}
