package roomescape.auth.dto;

public record MemberDetailResponse(
        Long id,
        String name,
        String email,
        String role
) {
    public boolean isAdmin() {
        return role.equals("ADMIN");
    }
}
