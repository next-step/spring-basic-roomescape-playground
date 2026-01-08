package roomescape.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiError {
	BAD_REQUEST_INVALID_INPUT(HttpStatus.BAD_REQUEST, 40001, "잘못된 요청입니다."),
	BAD_REQUEST_ILLEGAL_STATE(HttpStatus.BAD_REQUEST, 40002, "요청을 처리할 수 없습니다."),
	NOT_FOUND_RESOURCE(HttpStatus.NOT_FOUND, 40401, "리소스를 찾을 수 없습니다."),
	UNAUTHORIZED_MISSING_TOKEN(HttpStatus.UNAUTHORIZED, 40101, "토큰이 없습니다."),
	UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 40102, "토큰이 유효하지 않습니다."),
	UNAUTHORIZED_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 40103, "토큰이 만료되었습니다."),
	FORBIDDEN_ADMIN_ONLY(HttpStatus.FORBIDDEN, 40301, "관리자 권한이 필요합니다."),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50000, "서버 오류가 발생했습니다.");

	private final HttpStatus httpStatus;
	private final int code;
	private final String message;

	ApiError(HttpStatus httpStatus, int code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}
}



