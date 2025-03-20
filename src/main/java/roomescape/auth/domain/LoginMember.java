package roomescape.auth.domain;

public record LoginMember(
        Long id,
        String name,
        String email,
        String role
) {
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }

    public boolean isNotAdmin() {
        return !"ADMIN".equals(role);
    }

    public boolean notHaveName(String requestName) {
        return !name.equals(requestName);
    }
}
