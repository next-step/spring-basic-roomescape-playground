package roomescape.member;

public class MemberRequest {
    private String name;
    private String email;
    private String password;

    public String getName() {
        return name;
    }

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

    public String getPassword() {
        return password;
    }
}
