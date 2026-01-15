package auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.member.Role;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

import java.util.List;

@Profile("!test")
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
    public void run(String... args) {
        memberRepository.save(new Member("어드민", "admin@email.com", "password", Role.ADMIN));
        memberRepository.save(new Member("브라운", "brown@email.com", "password", Role.USER));

        themeRepository.save(new Theme("테마1", "테마1입니다."));
        themeRepository.save(new Theme("테마2", "테마2입니다."));
        themeRepository.save(new Theme("테마3", "테마3입니다."));

        List.of("10:00", "12:00", "14:00", "16:00", "18:00", "20:00")
                .forEach(t -> timeRepository.save(new Time(t)));
    }
}
