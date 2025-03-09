package roomescape.member;

public record LoginCheckResponse(
        String name
) {
    public LoginCheckResponse(Member member) {
        this(member.getName());
    }
}
