package roomescape.domain.theme.web.controller;

import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.domain.auth.principal.LoginMember;
import roomescape.domain.auth.web.support.annotation.AdminOnly;
import roomescape.domain.auth.web.support.annotation.Login;
import roomescape.domain.auth.web.support.annotation.Public;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.theme.service.ThemeService;
import roomescape.domain.theme.web.dto.ThemeRequest;
import roomescape.domain.theme.web.dto.ThemeResponse;
import roomescape.global.exception.ConflictException;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
public class ThemeController {

    private final ThemeService themeService;

    public ThemeController(ThemeService themeService) {
        this.themeService = themeService;
    }

    @AdminOnly
    @PostMapping("/themes")
    public ResponseEntity<ThemeResponse> createTheme(
            @Valid @RequestBody ThemeRequest request,
            @Login LoginMember loginMember
    ) {
        Theme newTheme;

        try {
            newTheme = themeService.saveTheme(loginMember.id(), request.name(), request.description());
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(loginMember.id(), Map.of("name", request.name()), "이미 존재하는 테마 이름입니다.");
        }

        return ResponseEntity.created(URI.create("/themes/" + newTheme.getId())).body(ThemeResponse.from(newTheme));
    }

    @Public
    @GetMapping("/themes")
    public List<ThemeResponse> list() {
        return themeService.findAllTheme().stream().map(ThemeResponse::from).toList();
    }

    @AdminOnly
    @DeleteMapping("/themes/{id}")
    public ResponseEntity<Void> deleteTheme(
            @PathVariable(name = "id") Long themeId,
            @Login LoginMember loginMember
    ) {
        themeService.deleteTheme(loginMember.id(), themeId);
        return ResponseEntity.noContent().build();
    }
}
