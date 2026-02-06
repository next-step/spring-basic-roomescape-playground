package missionAuth;

import roomescape.member.Role;

public record JwtDto(Long id, String name, Role role) {
}
