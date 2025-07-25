package roomescape.theme;

import java.sql.PreparedStatement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ThemeDao {
    private final JdbcTemplate jdbcTemplate;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Theme save(Theme theme) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO theme(name, description) VALUES (?, ?)",
                    new String[]{"id"});
            ps.setString(1, theme.getName());
            ps.setString(2, theme.getDescription());
            return ps;
        }, keyHolder);

        return new Theme(keyHolder.getKey().longValue(), theme.getName(), theme.getDescription());
    }

    public List<Theme> findAll() {
        return jdbcTemplate.query("SELECT * FROM theme where deleted = false", (rs, rowNum) -> new Theme(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description")
        ));
    }

    public Theme findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM theme WHERE id = ? AND deleted = false",
                (rs, rowNum) -> new Theme(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getString("description")
                ), id);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("UPDATE theme SET deleted = true WHERE id = ?", id);
    }
}
