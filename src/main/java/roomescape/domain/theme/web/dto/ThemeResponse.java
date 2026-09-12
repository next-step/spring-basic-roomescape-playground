package roomescape.domain.theme.web.dto;

import roomescape.domain.theme.entity.Theme;

public record ThemeResponse(
        Long id,
        String name,
        String description
) {

    public static ThemeResponse from(Theme theme) {
        return new ThemeResponse(theme.getId(), theme.getName(), theme.getDescription());
    }
}
