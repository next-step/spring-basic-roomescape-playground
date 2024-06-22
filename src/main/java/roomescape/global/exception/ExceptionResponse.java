package roomescape.global.exception;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    private int code = HttpStatus.BAD_REQUEST.value();
    private Object error;

    public ExceptionResponse(int code, Object error) {
        this.code = code;
        this.error =error;
    }

    public int getCode() {
        return code;
    }

    public Object getError() {
        return error;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setError(Object error) {
        this.error = error;
    }
}
