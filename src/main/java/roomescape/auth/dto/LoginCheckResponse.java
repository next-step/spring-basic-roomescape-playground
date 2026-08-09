package roomescape.auth.dto;

import roomescape.auth.domain.LoginMember;

public record LoginCheckResponse(
        String name
) {

    public static LoginCheckResponse from(LoginMember loginMember) {
        return new LoginCheckResponse(loginMember.name());
    }
}
