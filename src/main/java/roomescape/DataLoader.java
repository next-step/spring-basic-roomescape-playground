package roomescape;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRepository;

@Profile("default") // 배포 환경 -> "prod", 로컬 환경 -> "default"
@Component
public abstract class DataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;

    public DataLoader(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (memberRepository.count() == 0) {
            Member admin = new Member("어드민", "admin@email.com", "password", "ADMIN");
            Member brown = new Member("브라운", "brown@email.com", "password", "USER");
            memberRepository.save(admin);
            memberRepository.save(brown);
            System.out.println("초기 사용자 정보가 등록되었습니다.");
        }
    }
}
