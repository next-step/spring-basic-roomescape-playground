package roomescape.exception;

public record CustomExceptionResponse(
        int statusCode,
        String exceptionMessage
) {
}
