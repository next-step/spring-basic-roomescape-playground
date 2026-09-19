package roomescape.reservation.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;
import roomescape.time.dto.AvailableTime;
import roomescape.time.repository.TimeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationAvailabilityServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TimeRepository timeRepository;

    @InjectMocks
    private ReservationAvailabilityService reservationAvailabilityService;

    @Test
    void marks_reserved_times_as_booked() {
        LocalDate date = LocalDate.of(2024, 3, 2);
        Theme theme = new Theme(1L, "theme", "description");
        Time bookedTime = new Time(1L, LocalTime.of(10, 0));
        Time availableTime = new Time(2L, LocalTime.of(12, 0));
        Reservation reservation = new Reservation(1L, 1L, "member", date, bookedTime, theme);

        when(reservationRepository.findByDateAndThemeId(date, theme.getId())).thenReturn(List.of(reservation));
        when(timeRepository.findAll()).thenReturn(List.of(bookedTime, availableTime));

        List<AvailableTime> result = reservationAvailabilityService.findAvailableTimes(date, theme.getId());

        assertThat(result).containsExactly(
                new AvailableTime(1L, LocalTime.of(10, 0), true),
                new AvailableTime(2L, LocalTime.of(12, 0), false)
        );
    }
}
