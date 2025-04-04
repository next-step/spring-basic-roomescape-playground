package roomescape.member.dto;

import roomescape.member.enums.Role;

public record MemberResponse(Long id, String name, String email, Role role) {
}
