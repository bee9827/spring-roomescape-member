package roomescape.theme.controller.dto;


import lombok.Builder;
import roomescape.theme.domain.Theme;

@Builder
public record ThemeResponse(
        Long id,
        String name,
        String description,
        String thumbnail
) {
    public static ThemeResponse from(Theme theme) {
        return ThemeResponse.builder()
                .id(theme.getId())
                .name(theme.getName())
                .description(theme.getDescription())
                .thumbnail(theme.getThumbnail())
                .build();
    }
}
