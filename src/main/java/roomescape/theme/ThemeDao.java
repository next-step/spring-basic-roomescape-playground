package roomescape.theme;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeDao {
    private JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT * FROM theme where deleted = false", (rs, rowNum) -> new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description")
        ));
    }

    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            var ps = connection.prepareStatement("INSERT INTO theme(name, description) VALUES (?, ?)", new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            return ps;
        }, keyHolder);

        return new Theme(keyHolder.getKey().longValue(), theme.getName(), theme.getDescription());
    }

    public Optional<Theme> findById(Long id) {
        return jdbcTemplate.query(
                "SELECT id, name, description FROM theme WHERE id = ? AND deleted = false",
                (resultSet, rowNumber) -> new Theme(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description")
                ),
                id
        ).stream().findFirst();
    }

    public int deleteById(Long id) {
        return jdbcTemplate.update("UPDATE theme SET deleted = true WHERE id = ? AND deleted = false", id);
    }
}
