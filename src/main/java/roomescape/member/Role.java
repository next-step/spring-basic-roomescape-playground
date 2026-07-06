package roomescape.member;

public enum Role {
    ADMIN(3), MANAGER(2), GUEST(1);

    private final int level;

    Role(int level) {
        this.level = level;
    }

    public static Role from(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("존재하지 않는 권한입니다: " + role);
        }
    }

    public boolean isAuthorized(Role requiredRole) {
        return this.level >= requiredRole.level;
    }
}
