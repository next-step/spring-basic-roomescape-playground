package roomescape.auth.domain;

import roomescape.member.entity.Role;

public record LoginMember(
        Long id,
        String name,
        String role
) {
    public boolean isAdmin() {
        return this.role.equals(Role.ADMIN.name());
    }
}
