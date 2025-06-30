package roomescape.theme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;
import roomescape.theme.service.dto.result.PopularThemeResult;

import java.util.List;

@RestController
@RequestMapping("/themes")
@RequiredArgsConstructor
public class ThemeApiController {
    private final ThemeService themeService;

    @GetMapping("/popular")
    public ResponseEntity<List<PopularThemeResult>> getPopularThemes(){
        return ResponseEntity.ok(themeService.getPopularThemes());
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        return ResponseEntity.ok(themeService.findAll());
    }
}
