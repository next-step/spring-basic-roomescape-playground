package roomescape.time;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Time {

	@Id
	@GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
	private Long id;

	@Column(name = "time_value", nullable = false, length = 20)
	private String value;

	@ColumnDefault("false")
	@Column(name = "deleted", nullable = false)
	private boolean deleted;

    protected Time() {
    }

	private Time(Long id, String value) {
		this.id = id;
		this.value = value;
		this.deleted = false;
	}

	public Time(String value) {
		this(null, value);
	}

	public void markAsDeleted() {
		this.deleted = true;
	}

	public Long getId() {
		return id;
	}

	public String getValue() {
		return value;
	}
}
