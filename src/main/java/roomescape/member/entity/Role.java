package roomescape.member.entity;

public enum Role {

    USER,
    ADMIN;

    public boolean isAdmin() {
        return this == ADMIN;
    }
}
