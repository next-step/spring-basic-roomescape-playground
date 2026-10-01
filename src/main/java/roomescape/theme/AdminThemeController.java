package roomescape.theme;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class AdminThemeController {

    private final ThemeService themeService;

    public AdminThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/admin/themes")
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest request) {
        ThemeResponse newTheme = themeService.save(request);

        return ResponseEntity.created(URI.create("/admin/themes/" + newTheme.getId())).body(newTheme);
    }

    @DeleteMapping("/admin/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
