package roomescape.auth;

import roomescape.member.MemberRole;

public class LoginMemberInfo {
    private final Long id;
    private final String name;
    private final String email;
    private final MemberRole role;

    public LoginMemberInfo(Long id, String name, String email, MemberRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public MemberRole getRole() {
        return role;
    }

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}
