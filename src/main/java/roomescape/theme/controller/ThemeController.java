package roomescape.theme.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.member.auth.AdminOnly;
import roomescape.theme.dto.ThemeRequest;
import roomescape.theme.dto.ThemeResponse;
import roomescape.theme.service.ThemeResult;
import roomescape.theme.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
public class ThemeController {
    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @PostMapping("/themes")
    @AdminOnly
    public ResponseEntity<ThemeResponse> createTheme(@RequestBody ThemeRequest request) {
        ThemeResult theme = themeService.save(request.name(), request.description());
        return ResponseEntity.created(URI.create("/themes/" + theme.id())).body(toResponse(theme));
    }

    @GetMapping("/themes")
    public ResponseEntity<List<ThemeResponse>> list() {
        return ResponseEntity.ok(themeService.findAll().stream()
                .map(this::toResponse)
                .toList());
    }

    @DeleteMapping("/themes/{id}")
    @AdminOnly
    public ResponseEntity<Void> deleteTheme(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ThemeResponse toResponse(ThemeResult result) {
        return new ThemeResponse(result.id(), result.name(), result.description());
    }
}
