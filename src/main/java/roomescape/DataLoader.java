package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Component
@Profile("!test")
public class DataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 사용자 정보만 초기화
        memberRepository.save(new Member("username", "user@example.com", "password", "USER"));
        // 다른 초기화 작업 수행 가능
    }
}
