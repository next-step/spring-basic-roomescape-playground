package roomescape.auth.domain;

import roomescape.member.Role;

public record LoginMember(
        Long id,
        String name,
        String email,
        Role role
) {
    public boolean isAdmin() {
        return role.isAdmin();
    }

    public boolean isNotAdmin() {
        return role.isNotAdmin();
    }

    public boolean notHaveName(String requestName) {
        return !name.equals(requestName);
    }
}
