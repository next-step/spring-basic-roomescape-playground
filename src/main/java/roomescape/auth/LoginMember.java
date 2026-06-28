package roomescape.auth;

import roomescape.member.Member;
import roomescape.member.Role;

public class LoginMember {
    private final Long id;
    private final String name;
    private final Role role;

    public LoginMember(Long id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public static LoginMember from(Member member) {
        return new LoginMember(member.getId(), member.getName(), member.getRole());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }
}
