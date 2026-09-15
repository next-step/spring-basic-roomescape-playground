package roomescape.theme.controller.customer;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;

import java.util.List;

@RestController
public class CustomerThemeController {
    private final ThemeDao themeDao;

    public CustomerThemeController(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> list() {
        return ResponseEntity.ok(themeDao.findAll());
    }
}
