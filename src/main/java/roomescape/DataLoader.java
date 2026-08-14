package roomescape;


import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Profile("!test") //테스트 환경이 아닐때 실행되게 하는 기능
@Component
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public DataLoader(MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. 회원 저장 (ID: 1, 2)
        memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        memberRepository.save(new Member("브라운", "brown@email.com", "password", "USER"));

        // 2. 테마 저장
        themeRepository.save(new Theme("테마1", "테마1입니다."));
        themeRepository.save(new Theme("테마2", "테마2입니다."));
        themeRepository.save(new Theme("테마3", "테마3입니다."));

        // 3. 시간 저장
        timeRepository.save(new Time("10:00"));
        timeRepository.save(new Time("12:00"));
        timeRepository.save(new Time("14:00"));
        timeRepository.save(new Time("16:00"));
        timeRepository.save(new Time("18:00"));
        timeRepository.save(new Time("20:00"));
    }
}
