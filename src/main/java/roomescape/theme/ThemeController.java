package roomescape.theme;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        // 서비스 계층을 통해 비즈니스 로직 처리
        Theme newTheme = themeService.create(theme);
        return ResponseEntity.created(URI.create("/themes/" + newTheme.getId())).body(newTheme);
    }

    @GetMapping
    public ResponseEntity<List<Theme>> list() {
        List<Theme> themes = themeService.findAll();
        return ResponseEntity.ok(themes);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
