package roomescape.theme;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.exception.InvalidThemeException;
import roomescape.exception.NotFoundThemeException;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {
    private static final int MAX_THEME_LENGTH = 255;
    private ThemeDao themeDao;

    public ThemeController(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @PostMapping("/themes")
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        if (theme.getName() == null
                || theme.getName().isBlank()
                || theme.getName().length() > MAX_THEME_LENGTH
                || theme.getDescription() == null
                || theme.getDescription().isBlank()
                || theme.getDescription().length() > MAX_THEME_LENGTH) {
            throw new InvalidThemeException("테마 정보를 올바르게 입력해야 합니다.");
        }
        Theme newTheme = themeDao.save(theme);
        return ResponseEntity.created(URI.create("/themes/" + newTheme.getId())).body(newTheme);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> list() {
        return ResponseEntity.ok(themeDao.findAll());
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        int deletedCount = themeDao.deleteById(id);

        if (deletedCount == 0) {
            throw new NotFoundThemeException("삭제할 테마를 찾을 수 없습니다.");
        }

        return ResponseEntity.noContent().build();
    }
}
