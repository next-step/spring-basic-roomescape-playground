package roomescape.auth.dto;

import roomescape.member.MemberResponse;

public record MemberInfo(
        Long id,
        String name,
        String email,
        String role
) {
    public static MemberInfo from(MemberResponse memberResponse) {
        return new MemberInfo(memberResponse.id(), memberResponse.name(), memberResponse.email(), memberResponse.role());
    }
}