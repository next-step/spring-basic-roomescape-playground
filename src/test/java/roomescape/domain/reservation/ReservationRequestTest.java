package roomescape.domain.reservation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.web.dto.ReservationRequest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ReservationRequestTest {

    private final String name = "Alice";
    private final LocalDate date = LocalDate.now();
    private final Long theme = 1L;
    private final Long time = 1L;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void date필드가_null인_ReservationRequest를_생성하면_예외를_던진다() {
        // given
        ReservationRequest request = new ReservationRequest(name, null, theme, time);

        // when
        Set<ConstraintViolation<ReservationRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void date필드가_과거_날짜인_ReservationRequest를_생성하면_예외를_던진다() {
        // given
        ReservationRequest request = new ReservationRequest(name, LocalDate.of(1, 1, 1), theme, time);

        // when
        Set<ConstraintViolation<ReservationRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void theme필드가_null인_ReservationRequest를_생성하면_예외를_던진다() {
        // given
        ReservationRequest request = new ReservationRequest(name, date, null, time);

        // when
        Set<ConstraintViolation<ReservationRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void time필드가_null인_ReservationRequest를_생성하면_예외를_던진다() {
        // given
        ReservationRequest request = new ReservationRequest(name, date, theme, null);

        // when
        Set<ConstraintViolation<ReservationRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void 모든_검증조건을_통과한_ReservationRequest() {
        // given
        ReservationRequest request = new ReservationRequest(name, date, theme, time);

        // when
        Set<ConstraintViolation<ReservationRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).isEmpty();
    }
}
