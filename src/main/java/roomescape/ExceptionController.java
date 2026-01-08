package roomescape;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import roomescape.common.ApiError;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionController {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException e) {
		return build(ApiError.BAD_REQUEST_INVALID_INPUT);
	}

	@ExceptionHandler({IllegalStateException.class})
	public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException e) {
		return build(ApiError.BAD_REQUEST_ILLEGAL_STATE);
	}

	@ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
	public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException e) {
		return build(ApiError.NOT_FOUND_RESOURCE);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<Map<String, Object>> handleNotReadable(HttpMessageNotReadableException e) {
		return build(ApiError.BAD_REQUEST_INVALID_INPUT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleUnknown(Exception e) {
		e.printStackTrace();
		return build(ApiError.INTERNAL_SERVER_ERROR);
	}

	private ResponseEntity<Map<String, Object>> build(ApiError apiError) {
		Map<String, Object> body = new HashMap<>();
		body.put("code", apiError.getCode());
		body.put("message", apiError.getMessage());
		return ResponseEntity.status(apiError.getHttpStatus()).body(body);
	}
}
