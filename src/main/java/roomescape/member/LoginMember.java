package roomescape.member;

public record LoginMember(
        Long id, String name, String password, String email
) {
}
