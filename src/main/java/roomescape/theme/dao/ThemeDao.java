package roomescape.theme.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.theme.domain.Theme;

import java.util.List;
import java.util.Optional;

@Repository
public class ThemeDao {

    private static final RowMapper<Theme> THEME_ROW_MAPPER = (rs, rowNum) ->
            new Theme(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description")
            );

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

    public void deleteById(Long id) {
        jdbcTemplate.update("UPDATE theme SET deleted = true WHERE id = ?", id);
    }

    public Optional<Theme> findById(Long themeId) {
        try {
            String selectById = "SELECT id, name, description FROM theme WHERE id = ?";
            Theme theme = jdbcTemplate.queryForObject(selectById, THEME_ROW_MAPPER, themeId);
            return Optional.ofNullable(theme);
        } catch (EmptyResultDataAccessException emptyResultDataAccessException) {
            return Optional.empty();
        }
    }
}
