package roomescape.member;

public record LoginMember(
        Long id,
        String name,
        String role
) {
    public static LoginMember from(Member member) {
        return new LoginMember(
                member.getId(),
                member.getName(),
                member.getRole()
        );
    }
}
