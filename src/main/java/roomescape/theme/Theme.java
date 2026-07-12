package roomescape.theme;

public record Theme(Long id, String name, String description) {
    public Theme(String name, String description) {
        this(null, name, description);
    }
}
