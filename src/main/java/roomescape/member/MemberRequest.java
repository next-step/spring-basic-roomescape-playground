package roomescape.member;

import org.springframework.util.StringUtils;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

public record MemberRequest(
        String name,
        String email,
        String password) {

    public MemberRequest {
        if (!StringUtils.hasText(name)) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "이름은 필수입니다");
        }
        if (!StringUtils.hasText(email) || !email.contains("@")) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "이메일 형식이 맞지 않습니다");
        }
        if (!StringUtils.hasText(password)) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "비밀번호는 필수입니다");
        }
    }

    public Member toEntity() {
        return new Member(name, email, password, Role.USER);
    }
}
