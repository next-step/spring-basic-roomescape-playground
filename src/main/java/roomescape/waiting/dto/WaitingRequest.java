package roomescape.waiting.dto;

public record WaitingRequest(
        String date,
        Long time,
        Long theme
) {

    @Override
    public String date() {
        return date;
    }

    @Override
    public Long time() {
        return time;
    }

    @Override
    public Long theme() {
        return theme;
    }
}
