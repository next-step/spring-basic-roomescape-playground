package roomescape.waiting;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import roomescape.theme.Theme;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Theme theme;
    private String date;
    private String time;
}
