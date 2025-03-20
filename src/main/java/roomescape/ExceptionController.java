package roomescape;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException e) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", String.valueOf(e.getStatusCode().value()));
        errorResponse.put("error", e.getStatusCode().toString());
        errorResponse.put("message", e.getReason());

        return ResponseEntity.status(e.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(Exception e) {
        e.printStackTrace();

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("status", "500");
        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", "서버 내부 오류가 발생했습니다.");

        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
