package roomescape.global.excpetion;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("로그인 정보를 찾을 수 없습니다.");
    }
}
