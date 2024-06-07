package roomescape.member;

public class MemberFixture {

    public static Member memberWithName(String name) {
        return new Member(name, "email", "password", "USER");
    }
}
