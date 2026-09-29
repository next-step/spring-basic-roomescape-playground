package roomescape.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.member.entity.Member;
import roomescape.domain.member.repository.MemberRepository;
import roomescape.global.exception.ConflictException;

import java.util.HashMap;
import java.util.Map;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member createMember(String nickname, String email, String password) {

        if (memberRepository.existsByEmail(email)) {
            throw new ConflictException(null, Map.of("email", email), "이미 가입된 정보입니다.");
        }

        if (memberRepository.existsByNickname(nickname)) {
            throw new ConflictException(null, Map.of("nickname", nickname), "이미 사용 중인 닉네임입니다.");
        }

        return memberRepository.save(new Member(nickname, email, password, "USER"));
    }

    public Map<String, String> determineDuplicate(String email, String nickname) {
        Map<String, String> result = new HashMap<>();
        if (memberRepository.existsByNickname(nickname)) {
            result.put("key", "nickname");
            result.put("value", nickname);
            result.put("cause", "이미 사용 중인 닉네임입니다.");
        }
        if (memberRepository.existsByEmail(email)) {
            result.put("key", "email");
            result.put("value", email);
            result.put("cause", "이미 가입된 정보입니다.");
        }

        return result;
    }
}
