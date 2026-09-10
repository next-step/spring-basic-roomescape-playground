package roomescape;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Executable;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

    private final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Executable executable = e.getParameter().getExecutable();
        String where = executable.getDeclaringClass().getSimpleName() + "." + executable.getName();

        String detail = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> "%s = %s' (%s)".formatted(fe.getField(), fe.getRejectedValue(), fe.getDefaultMessage()))
                .collect(Collectors.joining(","));

        log.info("[{}] {}", where, detail);

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Void> handleBadRequestException(BadRequestException e, HandlerMethod handlerMethod) {
        String where = handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName();
        log.info("[{}] userId:{} rejectedInputs:{} {}", where, e.getUserId(), e.getRejectedInputs(), e.getMessage());
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Void> handleNotFoundException(NotFoundException e, HandlerMethod handlerMethod) {
        String where = handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName();
        log.info("[{}] userId:{} rejectedInputs:{} {}", where, e.getUserId(), e.getRejectedInputs(), e.getMessage());
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Void> handleUnauthorizedException(UnauthorizedException e, HandlerMethod handlerMethod) {
        String where = handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName();
        log.info("[{}] {}",  where, e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(Exception e, HttpServletRequest request) {
        log.error("[{} {}] 처리되지 않은 예외", request.getMethod(), request.getRequestURI(), e);
        return ResponseEntity.internalServerError().build();
    }
}
