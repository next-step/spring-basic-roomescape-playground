package roomescape.global.excpetion;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("해당 리소스에 대한 접근 권한이 없습니다.");
    }
}
