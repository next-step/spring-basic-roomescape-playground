package roomescape.domain.theme;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> createTheme(@Valid @RequestBody ThemeRequest request) {
        Theme newTheme = themeService.saveTheme(request.name(), request.description());
        return ResponseEntity.created(URI.create("/themes/" + newTheme.getId())).body(ThemeResponse.from(newTheme));
    }

    @GetMapping("/themes")
    public List<ThemeResponse> list() {
        return themeService.findAllTheme().stream().map(ThemeResponse::from).toList();
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
