package roomescape.member;

public enum Role {
    ADMIN, USER;


    public boolean isAdmin() {
        return this == ADMIN;
    }
}
