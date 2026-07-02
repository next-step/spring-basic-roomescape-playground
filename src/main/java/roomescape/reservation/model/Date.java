package roomescape.reservation.model;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Embeddable
public class Date {
    private String value;

    public Date(String value) {
        validate(value);
        this.value = value;
    }

    public Date() {

    }

    private void validate(String value) {
        try {
            LocalDate.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }
    }

    public String getValue() {
        return value;
    }
}
