package roomescape;

import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    private final ApplicationContext applicationContext;

    public ExceptionController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException e) {
        // 참고: org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler#handleExceptionInternal
        return e.updateAndGetBody(applicationContext, LocaleContextHolder.getLocale());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleRuntimeException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().build();
    }
}
