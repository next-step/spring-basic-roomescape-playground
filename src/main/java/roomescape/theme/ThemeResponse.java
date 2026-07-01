package roomescape.theme;

public record ThemeResponse(
        Long id,
        String name,
        String description
) {
    public Theme toEntity() {
        return new Theme(id, name, description);
    }
}
