package roomescape.domain.time;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.domain.time.web.dto.TimeRequest;

import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeRequestTest {

    private Validator validator;

    private LocalTime value = LocalTime.of(3, 0);

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    void value필드가_null값인_TimeRequest를_생성하면_예외를_던진다() {
        // given
        TimeRequest request = new TimeRequest(null);

        // when
        Set<ConstraintViolation<TimeRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(1);
    }

    @Test
    void 정상적으로_TimeRequest를_생성() {
        // given
        TimeRequest request = new TimeRequest(value);

        // when
        Set<ConstraintViolation<TimeRequest>> violations = validator.validate(request);

        // then
        assertThat(violations).hasSize(0);
        assertThat(request.value()).isEqualTo(value);
    }
}
