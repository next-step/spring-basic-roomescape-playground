package roomescape.fixture;

import roomescape.auth.dto.LoginMember;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.repository.MemberRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;
import roomescape.time.domain.Time;
import roomescape.time.repository.TimeRepository;

import java.time.LocalTime;

public class FixtureGenerator {

    private final MemberRepository memberRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;

    public FixtureGenerator(MemberRepository memberRepository, TimeRepository timeRepository, ThemeRepository themeRepository) {
        this.memberRepository = memberRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
    }

    public Theme createTheme() {
        Theme theme = new Theme("커스텀테마1", "커스텀테마 입니다.");
        return themeRepository.save(theme);
    }

    public Time createTime() {
        Time time = new Time(LocalTime.of(22, 0));
        return timeRepository.save(time);
    }

    public LoginMember createLoginMember(String name, String email) {
        Member member = new Member(name, email, "password", Role.USER);
        Member savedMember = memberRepository.save(member);
        return new LoginMember(savedMember.getId(), savedMember.getName(), savedMember.getEmail(), savedMember.getRole());
    }

    public Member createMember(String name, String email) {
        Member member = new Member(name, email, "password", Role.USER);
        return memberRepository.save(member);
    }
}
