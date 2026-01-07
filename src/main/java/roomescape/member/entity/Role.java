package roomescape.member.entity;

import java.util.Arrays;

public enum Role {
    USER,
    ADMIN;

    public static Role find(String roleName) {
        return Arrays.stream(values())
                .filter(role -> role.name().equalsIgnoreCase(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 권한입니다: " + roleName));
    }
}

