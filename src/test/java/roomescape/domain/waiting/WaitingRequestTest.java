package roomescape.domain.waiting;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.waiting.web.dto.WaitingRequest;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class WaitingRequestTest {

    private Validator validator;

    private final LocalDate date = LocalDate.now();
    private final Long theme = 1L;
    private final Long time = 1L;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void date필드가_null인_WaitingRequest를_생성하면_예외를_던진다() {
        // given
        WaitingRequest request = new WaitingRequest(null, theme, time);

        // when
        Set<ConstraintViolation<WaitingRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void date필드가_과거인_WaitingRequest를_생성하면_예외를_던진다() {
        // given
        WaitingRequest request = new WaitingRequest(date.minusDays(1), theme, time);

        // when
        Set<ConstraintViolation<WaitingRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void theme필드가_null인_WaitingRequest를_생성하면_예외를_던진다() {
        // given
        WaitingRequest request = new WaitingRequest(date, null, time);

        // when
        Set<ConstraintViolation<WaitingRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void time필드가_null인_WaitingRequest를_생성하면_예외를_던진다() {
        // given
        WaitingRequest request = new WaitingRequest(date, theme, null);

        // when
        Set<ConstraintViolation<WaitingRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }
}
