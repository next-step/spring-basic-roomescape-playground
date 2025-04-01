package roomescape.waiting;

import java.time.LocalDate;

public record WaitingRequest(LocalDate date, long time, long theme) {
}
