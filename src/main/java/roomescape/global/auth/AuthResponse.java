package roomescape.global.auth;

import roomescape.member.Member;

public record AuthResponse(
    String name
) {

    public static AuthResponse from(Member member) {
        return new AuthResponse(member.getName());
    }
}
