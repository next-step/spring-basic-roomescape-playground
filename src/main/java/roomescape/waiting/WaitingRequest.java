package roomescape.waiting;

public record WaitingRequest(
        Long themeId,
        String date,
        Long timeId
) {
}
