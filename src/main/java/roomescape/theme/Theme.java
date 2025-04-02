package roomescape.theme;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Theme {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "description", nullable = false)
	private String description;
	@Column(name = "deleted", nullable = false, columnDefinition = "DEFAULT FALSE")
	private boolean deleted;

	protected Theme() {
	}

	private Theme(Long id, String name, String description, boolean deleted) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.deleted = deleted;
	}

	public static Theme ofDeletedFalse(String name, String description) {
		return new Theme(null, name, description, false);
	}

	public void martAsDeleted() {
		this.deleted = true;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
}
