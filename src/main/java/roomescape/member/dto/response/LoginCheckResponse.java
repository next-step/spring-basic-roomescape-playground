package roomescape.member.dto.response;

import roomescape.member.domain.Member;

public record LoginCheckResponse(
        String name
) {
    public LoginCheckResponse(Member member) {
        this(member.getName());
    }
}
