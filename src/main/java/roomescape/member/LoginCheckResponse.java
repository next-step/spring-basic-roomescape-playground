package roomescape.member;

public record LoginCheckResponse(
        String name
) {
    public static LoginCheckResponse from(LoginMember loginMember) {
        return new LoginCheckResponse(loginMember.name());
    }
}
