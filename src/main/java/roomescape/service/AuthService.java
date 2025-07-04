package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.common.TokenProvider;
import roomescape.controller.dto.LoginRequest;
import roomescape.service.dto.result.MemberResult;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    public String createToken(LoginRequest loginRequest) {
        MemberResult memberResult = memberService.findByEmailAndPassword(loginRequest.email(), loginRequest.password());
        return tokenProvider.generateToken(memberResult.id(), memberResult.role());
    }

    public MemberResult getMember(Long id) {
        return memberService.findById(id);
    }

}
