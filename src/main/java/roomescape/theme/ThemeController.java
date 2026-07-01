package roomescape.theme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {
    private final ThemeService themeService;

    @Autowired
    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest request) {
        ThemeResponse newTheme = themeService.createTheme(request);
        return ResponseEntity.created(URI.create("/themes/" + newTheme.id())).body(newTheme);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> list() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteTheme(id);
        return ResponseEntity.noContent().build();
    }
}
