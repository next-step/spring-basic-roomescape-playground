package roomescape.auth;

import roomescape.member.domain.Role;

public record LoginMember(
        String name,
        Role role
) {
}
