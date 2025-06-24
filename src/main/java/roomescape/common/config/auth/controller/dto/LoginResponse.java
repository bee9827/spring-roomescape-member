package roomescape.common.config.auth.controller.dto;

import roomescape.member.domain.Member;

public record LoginResponse(
        Long id,
        String email,
        String name
) {
    public LoginResponse(Member member) {
        this(member.getId(), member.getEmail(), member.getName());
    }
}
