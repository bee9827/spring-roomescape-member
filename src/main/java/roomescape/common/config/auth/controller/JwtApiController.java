package roomescape.common.config.auth.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.common.config.AuthMember;
import roomescape.common.config.auth.JwtProvider;
import roomescape.common.config.auth.service.dto.LoginRequest;
import roomescape.common.config.auth.service.dto.LoginResponse;
import roomescape.common.config.auth.service.AuthService;
import roomescape.common.util.CookieUtil;
import roomescape.member.domain.Member;

@RestController
@RequiredArgsConstructor
public class JwtApiController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(
            @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Cookie cookie = CookieUtil.createSessionCookie(JwtProvider.NAME, authService.createToken(loginRequest));
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
    public ResponseEntity<LoginResponse> check(@AuthMember Member member) {
        return ResponseEntity.ok(new LoginResponse(member));
    }

    /*
    Todo List
        [Post] logout
        [Post] login
        [Get] login/check
     */
}
