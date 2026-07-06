package roomescape.auth.domain;

import roomescape.member.domain.Role;

public record LoginMember(
        Long id,
        String name,
        String email,
        String role
) {
    public boolean isAdmin() {
        return this.role.equals(Role.ADMIN.name());
    }
}
