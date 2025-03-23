package roomescape.member;

public record LoginMember(Long id, String name, String email, String role) {

    public boolean isAdmin() {
        return this.role.equals("ADMIN");
    }
}
