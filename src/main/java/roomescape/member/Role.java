package roomescape.member;

import java.util.Arrays;
import roomescape.exception.InvalidRoleException;

public enum Role {

    ADMIN,
    USER;

    public static Role from(String role) {
        return Arrays.stream(values())
                .filter(roleName -> roleName.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(InvalidRoleException::new);
    }

}
