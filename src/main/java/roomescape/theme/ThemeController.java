package roomescape.theme;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme newTheme = themeService.create(theme);
        return ResponseEntity.created(URI.create("/themes/" + newTheme.getId())).body(newTheme);
    }

    @GetMapping
    public ResponseEntity<List<Theme>> list() {
        List<Theme> themes = themeService.findAll();
        return ResponseEntity.ok(themes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Theme> findThemeById(@PathVariable Long id) {
        Theme theme = themeService.findById(id);
        return ResponseEntity.ok(theme);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
