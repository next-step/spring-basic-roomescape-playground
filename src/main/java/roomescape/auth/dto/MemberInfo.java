package roomescape.auth.dto;

import roomescape.member.MemberResponse;

public record MemberInfo(
        Long id,
        String name,
        String email
) {
    public static MemberInfo from(MemberResponse memberResponse) {
        return new MemberInfo(memberResponse.getId(), memberResponse.getName(), memberResponse.getEmail());
    }
}