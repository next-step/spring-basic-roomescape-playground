package roomescape.reservation;

import jakarta.validation.constraints.NotNull;

 public record ReservationRequestDto(String name, @NotNull String date, @NotNull Long theme, @NotNull Long time) {}


