package roomescape.theme.service.dto.command;

import lombok.Builder;
import roomescape.theme.domain.Theme;

@Builder
public record ThemeCreateCommand(
        String name,
        String description,
        String thumbnail
) {
    public Theme toEntity(){
        return Theme.builder()
                .name(name)
                .description(description)
                .thumbnail(thumbnail)
                .build();
    }
}
