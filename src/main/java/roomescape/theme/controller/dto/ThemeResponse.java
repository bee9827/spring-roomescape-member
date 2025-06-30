package roomescape.theme.controller.dto;


import lombok.Builder;
import roomescape.theme.domain.Theme;
import roomescape.theme.service.dto.result.ThemeResult;

@Builder
public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeResponse from(ThemeResult themeResult) {
        return ThemeResponse.builder()
                .id(themeResult.id())
                .name(themeResult.name())
                .description(themeResult.description())
                .thumbnail(themeResult.thumbnail())
                .build();
    }
}
