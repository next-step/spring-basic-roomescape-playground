package roomescape.theme;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/themes")
public class ThemeController {
    private final ThemeRepository themeRepository;

    public ThemeController(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @PostMapping
    public ResponseEntity<Theme> createTheme(@RequestBody Theme theme) {
        Theme newTheme = themeRepository.save(theme);
        return ResponseEntity.created(URI.create("/themes/" + newTheme.getId())).body(newTheme);
    }

    @GetMapping
    public ResponseEntity<List<Theme>> list() {
        return ResponseEntity.ok(themeRepository.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
