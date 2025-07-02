package roomescape.service.dto.command;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record MemberCreateCommand(
        @Email(message = "이메일 공백") String email,
        @NotBlank(message = "비번 공백") String password,
        @NotBlank(message = "이름 공백") String name
) {
}
