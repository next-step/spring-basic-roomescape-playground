package roomescape.domain.member.web.dto;

import roomescape.domain.member.entity.Member;

public record MemberResponse (
        Long id, String name, String email
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getNickname(), member.getEmail());
    }
}
