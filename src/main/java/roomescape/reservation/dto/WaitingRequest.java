package roomescape.reservation.dto;

import org.jetbrains.annotations.NotNull;

public record WaitingRequest (
        @NotNull String date,
        @NotNull Long timeId,
        @NotNull Long themeId
) {

}
