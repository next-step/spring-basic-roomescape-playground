package roomescape.waiting;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WaitingResponse {
    private Long id;
    private Long themeId;
    private String date;
    private String time;
    private int waitingCount;
}
