package roomescape.auth.domain;

import roomescape.member.Role;

public record LoginMember(
        Long id,
        String name,
        String email,
        Role role
) {
    public boolean isAdmin() {
        return "ADMIN".equals(role.getDescription());
    }

    public boolean isNotAdmin() {
        return !"ADMIN".equals(role.getDescription());
    }

    public boolean notHaveName(String requestName) {
        return !name.equals(requestName);
    }
}
