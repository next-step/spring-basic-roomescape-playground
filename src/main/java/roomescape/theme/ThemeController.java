package roomescape.theme;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.AdminOnly;

import roomescape.NotFoundException;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {
    private ThemeDao themeDao;

    public ThemeController(ThemeDao themeDao) {
        this.themeDao = themeDao;
    }

    @PostMapping("/themes")
    @AdminOnly
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme newTheme = themeDao.save(theme);
        return ResponseEntity.created(URI.create("/themes/" + newTheme.getId())).body(newTheme);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<Theme>> list() {
        return ResponseEntity.ok(themeDao.findAll());
    }

    @DeleteMapping("/themes/{id}")
    @AdminOnly
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        if (!themeDao.deleteById(id)) {
            throw new NotFoundException("존재하지 않는 테마입니다.");
        }
        return ResponseEntity.noContent().build();
    }
}
