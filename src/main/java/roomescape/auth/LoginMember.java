package roomescape.auth;

import roomescape.member.Role;

public record LoginMember(
        String name,
        Role role
) {
}