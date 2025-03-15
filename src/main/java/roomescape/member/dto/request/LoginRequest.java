package roomescape.member.dto.request;

import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionMessage;

public record LoginRequest(
        String email,
        String password
) {
    public LoginRequest {
        validateEmailNotBlank(email);
        validatePasswordNotBlank(password);
    }

    private void validateEmailNotBlank(String email) {
        if (email == null || email.isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_EMAIL.getMessage());
        }
    }

    private void validatePasswordNotBlank(String password) {
        if (password == null || password.isBlank()) {
            throw new BadRequestException(ExceptionMessage.INVALID_PASSWORD.getMessage());
        }
    }
}
