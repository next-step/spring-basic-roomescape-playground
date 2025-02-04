package roomescape.auth;

public enum Role {
    ADMIN, USER;

    static boolean isAdmin(Role role) {
        return role == ADMIN;
    }
}
