package roomescape.common.config.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.config.LoginMember;
import roomescape.common.config.auth.JwtProvider;
import roomescape.common.config.auth.controller.dto.LoginRequest;
import roomescape.common.config.auth.controller.dto.LoginResponse;
import roomescape.common.config.auth.service.MemberAuthService;
import roomescape.common.util.CookieUtil;
import roomescape.member.domain.Member;

@RestController
@RequiredArgsConstructor
public class JwtApiController {
    private final MemberAuthService memberAuthService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Cookie cookie = CookieUtil.createSessionCookie(JwtProvider.NAME, memberAuthService.createToken(loginRequest));
        response.addCookie(cookie);

        return ResponseEntity.ok().build();

    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie deletedCookie = CookieUtil.deleteCookie(roomescape.common.config.auth.JwtProvider.NAME);
        response.addCookie(deletedCookie);  // max-age: 0
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/check")
    public ResponseEntity<LoginResponse> check(@LoginMember Member member) {
        return ResponseEntity.ok(new LoginResponse(member));
    }

    /*
    Todo List
        [Post] logout
        [Post] login
        [Get] login/check
     */
}
