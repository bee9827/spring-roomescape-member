package roomescape.common.config.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.config.auth.JwtProvider;
import roomescape.common.config.auth.service.dto.LoginRequest;
import roomescape.member.domain.Member;
import roomescape.member.service.MemberService;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public String createToken(LoginRequest loginRequest) {
        Member member = memberService.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return jwtProvider.generateToken(member.getId(), member.getRole());
    }

    public Member findMemberByToken(String token) {
        Long memberId = jwtProvider.getMemberId(token);
        return memberService.findById(memberId);
    }
}
