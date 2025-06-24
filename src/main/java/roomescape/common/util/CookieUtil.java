package roomescape.common.util;

import jakarta.servlet.http.Cookie;
import roomescape.common.config.auth.JwtProvider;
import roomescape.common.exception.RestApiException;
import roomescape.common.exception.status.AuthErrorStatus;

import java.util.Arrays;

public class CookieUtil {

    public static Cookie createSessionCookie(String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        return cookie;
    }

    public static Cookie deleteCookie(String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        return cookie;
    }

    public static String getValueByName(String name, Cookie[] cookies) {
        if (cookies == null) throw new RestApiException(AuthErrorStatus.NOT_FOUND_COOKIE);

        return Arrays.stream(cookies)
                .filter(c -> JwtProvider.NAME.equals(c.getName()))
                .findFirst()
                .orElseThrow(() -> new RestApiException(AuthErrorStatus.TOKEN_NOT_IN_COOKIE))
                .getValue();
    }
}
