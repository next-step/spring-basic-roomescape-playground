package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;

@Component
@Profile("test")
public class TestDataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;

    public TestDataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 테스트에 필요한 데이터 초기화
        memberRepository.save(new Member("testuser1", "test1@example.com", "password", "USER"));
        memberRepository.save(new Member("testuser2", "test2@example.com", "password", "USER"));
    }
}
