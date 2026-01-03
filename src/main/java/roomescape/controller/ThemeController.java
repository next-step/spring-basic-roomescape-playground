package roomescape.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import roomescape.auth.AdminRoute;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.service.ThemeService;

@RestController
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @AdminRoute
    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> create(@RequestBody ThemeRequest request) {
        ThemeResponse theme = themeService.create(request);
        return ResponseEntity.created(URI.create("/themes/" + theme.id())).body(theme);
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> list() {
        return ResponseEntity.ok(themeService.findAll());
    }

    @AdminRoute
    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
