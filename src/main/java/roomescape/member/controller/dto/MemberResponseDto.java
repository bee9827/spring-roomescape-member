package roomescape.member.controller.dto;

import roomescape.member.domain.Member;

public record MemberResponseDto(
    Long id,
    String email,
    String name
) {
    public MemberResponseDto(Member member) {
        this(member.getId(), member.getEmail(), member.getName());
    }
}
