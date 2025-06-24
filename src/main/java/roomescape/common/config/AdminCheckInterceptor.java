package roomescape.common.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.common.config.auth.JwtProvider;
import roomescape.common.config.auth.service.MemberAuthService;
import roomescape.common.exception.RestApiException;
import roomescape.common.util.CookieUtil;
import roomescape.member.domain.Member;
import roomescape.member.repository.MemberRepository;

@Component
@RequiredArgsConstructor
public class AdminCheckInterceptor implements HandlerInterceptor {
    private final MemberAuthService memberAuthService;

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) throws Exception {

        try {
            Cookie[] cookies = request.getCookies();
            String token = CookieUtil.getValueByName(JwtProvider.NAME, cookies);

            Member member = memberAuthService.findMemberByToken(token);
            member.validateAdmin();
            return true;
        }catch (RestApiException e) {
            response.setStatus(403);
            return false;
        }
    }
}
