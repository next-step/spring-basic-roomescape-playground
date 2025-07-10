package roomescape.theme;

import org.springframework.util.StringUtils;
import roomescape.exception.ErrorCode;
import roomescape.exception.RoomEscapeException;

public record ThemeRequest (
        String name,
        String description
){
    public ThemeRequest{
        if (!StringUtils.hasText(name)) {
            throw new RoomEscapeException(ErrorCode.VALIDATION_ERROR, "테마 이름은 필수입니다");
        }
    }

    public Theme toEntity() {
        return new Theme(name, description);
    }
}
