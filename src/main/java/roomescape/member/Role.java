package roomescape.member;

public enum Role {
    ADMIN, GUEST;

    public static Role from(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("존재하지 않는 권한입니다: " + role);
        }
    }
}
