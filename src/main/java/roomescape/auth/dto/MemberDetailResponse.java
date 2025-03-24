package roomescape.auth.dto;

import roomescape.member.Role;

public record MemberDetailResponse(
        Long id,
        String name,
        String email,
        Role role
) {}
