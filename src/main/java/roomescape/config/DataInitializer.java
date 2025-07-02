package roomescape.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import roomescape.member.Member;
import roomescape.member.MemberRepository;
import roomescape.theme.Theme;
import roomescape.theme.ThemeRepository;
import roomescape.time.Time;
import roomescape.time.TimeRepository;

@Component
public class DataInitializer {

    private final MemberRepository memberRepository;
    private final ThemeRepository themeRepository;
    private final TimeRepository timeRepository;

    public DataInitializer(MemberRepository memberRepository, ThemeRepository themeRepository, TimeRepository timeRepository) {
        this.memberRepository = memberRepository;
        this.themeRepository = themeRepository;
        this.timeRepository = timeRepository;
    }

    @PostConstruct
    @Transactional
    public void init() {
        // Member 데이터
        memberRepository.save(new Member("어드민", "admin@email.com", "password", "ADMIN"));
        memberRepository.save(new Member("클로이", "chloe@email.com", "password", "USER"));

        // Theme 데이터
        themeRepository.save(new Theme("공포", "매우 무서운 테마", "https://i.imgur.com/1.jpg"));
        themeRepository.save(new Theme("코믹", "매우 웃긴 테마", "https://i.imgur.com/2.jpg"));

        // Time 데이터
        timeRepository.save(new Time("10:00"));
        timeRepository.save(new Time("13:00"));
        timeRepository.save(new Time("15:00"));
    }
}
