package roomescape;

import org.jetbrains.annotations.Nullable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponseException;

public class ApiException extends ErrorResponseException {
    public ApiException(
            HttpStatusCode status, ProblemDetail body,
            @Nullable Throwable cause,
            @Nullable String messageDetailCode,
            @Nullable Object[] messageDetailArguments
    ) {
        super(status, body, cause, messageDetailCode, messageDetailArguments);
    }

    public static ApiException status(HttpStatusCode status) {
        return new ApiException(status, ProblemDetail.forStatus(status), null, null, null);
    }
}
