package roomescape.auth;

import roomescape.member.Member;

public class LoginCheckResponse {
    private final String name;

    public LoginCheckResponse(String name) {
        this.name = name;
    }

    public static LoginCheckResponse from(Member member) {
        return new LoginCheckResponse(member.getName());
    }

    public String getName() {
        return name;
    }
}
