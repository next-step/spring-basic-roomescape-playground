package roomescape.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException() {
        super("입력한 회원 정보를 찾을 수 없습니다.");
    }
}
