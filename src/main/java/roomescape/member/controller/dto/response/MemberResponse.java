package roomescape.member.controller.dto.response;

import roomescape.member.domain.Member;

public record MemberResponse(
    Long id,
    String email,
    String name
) {
    public MemberResponse(Member member) {
        this(member.getId(), member.getEmail(), member.getName());
    }
}
