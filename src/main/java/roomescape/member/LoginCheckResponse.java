package roomescape.member;

public record LoginCheckResponse (
        String name
) {
    public static LoginCheckResponse from(Member member) {
        return new LoginCheckResponse(member.getName());
    }
}
