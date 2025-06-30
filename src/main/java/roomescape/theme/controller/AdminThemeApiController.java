package roomescape.theme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.controller.dto.ThemeCreateRequest;
import roomescape.theme.controller.dto.ThemeResponse;
import roomescape.theme.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(AdminThemeApiController.BASE_URL)
public class AdminThemeApiController {
    public static final String BASE_URL = "admin/themes";
    private final ThemeService themeService;

    @PostMapping
    public ResponseEntity<ThemeResponse> create(
            @RequestBody
            ThemeCreateRequest themeCreateRequestDto
    ) {
        ThemeResponse themeResponse = ThemeResponse.from(themeService.save(themeCreateRequestDto.toCommand()));
        URI uri = URI.create(BASE_URL + "/" + themeResponse.id());
        return ResponseEntity.created(uri).body(themeResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponse> getThemeById(@PathVariable Long id) {
        return ResponseEntity.ok(ThemeResponse.from(themeService.findById(id)));
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponse>> getThemes() {
        List<ThemeResponse> themeResponses = themeService.findAll()
                .stream()
                .map(ThemeResponse::from)
                .toList();
        return ResponseEntity.ok(themeResponses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
