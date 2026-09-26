package roomescape.waiting.service;

import java.time.LocalDate;

public record WaitingCommand(LocalDate date, Long themeId, Long timeId) {
}
