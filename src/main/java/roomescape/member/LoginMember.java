package roomescape.member;

public record LoginMember(
        Long id,
        String name,
        String email,
        Role role
) {
    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
