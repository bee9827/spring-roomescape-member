package roomescape.theme.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.theme.controller.dto.ThemeRequestDto;
import roomescape.theme.controller.dto.ThemeResponseDto;
import roomescape.theme.service.ThemeService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ThemeApiController.BASE_URL)
public class ThemeApiController {
    public static final String BASE_URL = "/themes";
    private final ThemeService themeService;

    @PostMapping
    public ResponseEntity<ThemeResponseDto> create(
            @RequestBody
            ThemeRequestDto themeRequestDto
    ) {
        ThemeResponseDto themeResponseDto = new ThemeResponseDto(themeService.save(themeRequestDto));
        URI uri = URI.create(BASE_URL + "/" + themeResponseDto.id());
        return ResponseEntity.created(uri).body(themeResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThemeResponseDto> getThemeById(@PathVariable Long id) {
        ThemeResponseDto themeResponseDto = new ThemeResponseDto(themeService.findById(id));
        return ResponseEntity.ok(themeResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<ThemeResponseDto>> getThemes() {
        List<ThemeResponseDto> responseDtos = themeService.findAll()
                .stream()
                .map(ThemeResponseDto::new)
                .toList();

        return ResponseEntity.ok(responseDtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        themeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
