package roomescape.member.dto;

import roomescape.member.domain.Member;

public record LoginResponse(String name) {
    public static LoginResponse from(Member member) {
        return new LoginResponse(member.getName());
    }
}
