package roomescape.exception;

import java.time.LocalDateTime;

public record ErrorResponseBody(
        String message,
        LocalDateTime currentTime
) {
}
