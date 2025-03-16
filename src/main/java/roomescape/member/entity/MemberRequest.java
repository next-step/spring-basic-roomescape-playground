package roomescape.member.dto;


import java.util.regex.Pattern;
import roomescape.exception.CreateMemberFailException;

public class MemberRequest {
    private final String name;
    private String email;
    private final String password;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public MemberRequest(String name, String email, String password) {
        validateEmail(email);
        validatePassword(password);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new CreateMemberFailException("이메일은 필수 입력입니다.");
        }

        if (!Pattern.matches(EMAIL_REGEX, email)) {
            throw new CreateMemberFailException("유효하지 않은 이메일 형식입니다.");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력입니다.");
        }
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
