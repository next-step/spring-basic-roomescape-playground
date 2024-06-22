package roomescape.member.exception;

public class MemberNotFoundException extends ValidationException {
    public MemberNotFoundException( ) {
        super("유저를 찾을 수 없습니다.");
    }
}
