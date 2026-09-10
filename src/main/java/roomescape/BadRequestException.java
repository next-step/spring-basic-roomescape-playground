package roomescape;

import java.util.Map;

public class BadRequestException extends RuntimeException {

    private final Long userId;
    private final Map<String, Object> rejectedInputs;

    public BadRequestException(Long userId, Map<String, Object> rejectedInputs, String message) {
        super(message);
        this.userId = userId;
        this.rejectedInputs = rejectedInputs;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Map<String, Object> getRejectedInputs() {
        return this.rejectedInputs;
    }
}
