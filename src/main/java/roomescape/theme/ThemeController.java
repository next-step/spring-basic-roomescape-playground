package roomescape.theme;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThemeController {
    private final ThemeRepository themeRepository;

    public ThemeController(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest request) {
        Theme newTheme = themeRepository.save(new Theme(request.name(), request.description()));
        return ResponseEntity.created(URI.create("/themes/" + newTheme.getId()))
            .body(ThemeResponse.from(newTheme));
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> list() {
        List<ThemeResponse> result = themeRepository.findAll().stream()
            .map(ThemeResponse::from)
            .toList();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        if (!themeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        themeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
