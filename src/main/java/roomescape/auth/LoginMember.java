package roomescape.auth;

import roomescape.member.Role;

public record LoginMember(
        Long id,
        String name,
        String email,
        Role role
) {
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
    public boolean isUser() { return this.role == Role.USER; }
}
