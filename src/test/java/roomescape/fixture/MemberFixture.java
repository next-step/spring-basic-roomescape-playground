package roomescape.fixture;

import roomescape.member.entity.Member;
import roomescape.member.entity.Role;

@SuppressWarnings("NonAsciiCharacters")
public class MemberFixture {

    public static Member 관리자_관리자1_생성() {
        return Member.of("admin1", "admin1@email.com", "password", Role.ADMIN);
    }

    public static Member 멤버_멤버1_생성() {
        return Member.of("member1", "member1@email.com", "password", Role.USER);
    }

    public static Member 멤버_멤버2_생성() {
        return Member.of("member2", "member2@email.com", "password", Role.USER);
    }
}
