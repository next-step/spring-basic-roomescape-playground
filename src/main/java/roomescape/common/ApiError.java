package roomescape.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApiError {
	UNAUTHORIZED_MISSING_TOKEN(HttpStatus.UNAUTHORIZED, 40101, "토큰이 없습니다."),
	UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, 40102, "토큰이 유효하지 않습니다."),
	FORBIDDEN_ADMIN_ONLY(HttpStatus.FORBIDDEN, 40301, "관리자 권한이 필요합니다.");

	private final HttpStatus httpStatus;
	private final int code;
	private final String message;

	ApiError(HttpStatus httpStatus, int code, String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}
}



