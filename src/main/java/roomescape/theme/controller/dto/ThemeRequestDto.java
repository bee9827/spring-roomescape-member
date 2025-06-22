package roomescape.theme.controller.dto;

import roomescape.theme.domain.Theme;

public record ThemeRequestDto(
        String name,
        String description,
        String thumbnail
) {
    public Theme toEntity() {
        return Theme.builder()
                .name(name)
                .description(description)
                .thumbnail(thumbnail)
                .build();
    }
}
