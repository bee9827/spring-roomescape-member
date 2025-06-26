package roomescape.member.service.dto.result;

import lombok.Builder;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

@Builder
public record MemberResult(
        Long memberId,
        String email,
        String name,
        Role role
) {
    public static MemberResult from(Member member) {
        return MemberResult.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole())
                .build();
    }
}
