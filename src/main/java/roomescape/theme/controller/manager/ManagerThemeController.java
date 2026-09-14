package roomescape.theme.controller.manager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;

import java.net.URI;

@RestController
public class ManagerThemeController {
    private final ThemeDao themeDao;

    public ManagerThemeController(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @PostMapping("/manager/themes")
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme newTheme = themeDao.save(theme);
        return ResponseEntity.created(URI.create("/manager/themes/" + newTheme.getId())).body(newTheme);
    }

    @DeleteMapping("/manager/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeDao.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
