package roomescape.common.exception;

public record CustomExceptionResponse(
        int statusCode,
        String exceptionMessage
) {
}
