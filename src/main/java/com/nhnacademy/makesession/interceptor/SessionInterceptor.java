package com.nhnacademy.makesession.interceptor;

import static com.nhnacademy.makesession.session.SessionManager.SESSION_COOKIE_NAME;

import com.nhnacademy.makesession.service.impl.SessionHolder;
import com.nhnacademy.makesession.session.SessionManager;
import com.nhnacademy.makesession.session.impl.RedisSessionManager;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @author : 조재철
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
public class SessionInterceptor implements HandlerInterceptor {
    private final SessionManager sessionManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        Cookie sessionCookie = null;

        if (request.getCookies() != null) {
            sessionCookie = Arrays.stream(request.getCookies())
                                         .filter(cookie -> cookie.getName().equals(SESSION_COOKIE_NAME))
                                         .findAny()
                                         .orElse(null);
        }

        if (sessionCookie != null) {
            SessionHolder.setSessionId(sessionCookie.getValue());
        }

        sessionManager.setLastAccessedTime(System.currentTimeMillis());

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SessionHolder.removeSessionId();
    }
}

