package com.nhnacademy.makesession.session.impl;

import com.nhnacademy.makesession.aspect.annotation.SessionReset;
import com.nhnacademy.makesession.service.impl.SessionHolder;
import com.nhnacademy.makesession.session.SessionManager;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Redis를 이용한 Session의 기능을 가진 클래스입니다.
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */
@Profile("redis")
@Component
@RequiredArgsConstructor
public class RedisSessionManager implements SessionManager {

    private int sessionTimeoutSecond = 30 * 60;

    private long sessionCreationTime;
    private long sessionFinalRequestTime;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    @SessionReset
    public void setAttribute(String key, Object value) {
        redisTemplate.opsForHash().put(getId(), key, value);

        redisTemplate.expire(getId(), this.sessionTimeoutSecond, TimeUnit.SECONDS);
    }

    @Override
    @SessionReset
    public Object getAttribute(String key) {
        return redisTemplate.opsForHash().get(getId(), key);
    }

    @Override
    @SessionReset
    public void removeAttribute(String key) {
        redisTemplate.opsForHash().delete(getId(), key);
    }

    @Override
    public boolean isNew() {
        return getId() == null;
    }

    @Override
    public void invalidate() {
        Cookie sessionCookie = findCookie(SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            redisTemplate.delete(findCookie(SESSION_COOKIE_NAME).getValue());

            removeCookie(sessionCookie);

            this.sessionCreationTime = 0;
            this.sessionFinalRequestTime = 0;

            SessionHolder.removeSessionId();
        }
    }

    @Override
    public String getId() {
        return SessionHolder.getSessionId();
    }

    @Override
    public long getCreationTime() {
        return sessionCreationTime;
    } //세션이 생성된 시간을 January 1 ,1970 GMT 부터 long 형 밀리세컨드 값으로 반환

    @Override
    public void setCreationTime(long creationTime) {
        this.sessionCreationTime = creationTime;
    }

    @Override
    public long getLastAccessedTime() {
        return sessionFinalRequestTime;
    }  //웹 브라우저의 요청이 마지막으로 시도된 시간을 long 형 ms 값으로 반환

    @Override
    public void setLastAccessedTime(long currentTime) {
        this.sessionFinalRequestTime = currentTime;
    }

    @Override
    public void setMaxInactiveInterval(int second) {
        sessionTimeoutSecond = second;
    }  //세션을 유지할 시간을 초단위로 설정 합니다.

    @Override
    public int getMaxInactiveInterval() {
        return sessionTimeoutSecond;
    } //세션의 유효시간을 초 단위로 반환 합니다. 기본값은 30초 입니다.

    @Override
    public Cookie findCookie(String cookieName) {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        return Arrays.stream(request.getCookies())
                     .filter(cookie -> cookie.getName().equals(cookieName))
                     .findAny()
                     .orElse(null);
    }

    @Override
    public void removeCookie(Cookie cookie) {
        cookie.setMaxAge(0);
        cookie.setPath("/");

        HttpServletResponse response =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        response.addCookie(cookie);

    }

    @Override
    public void resetSessionExpireTime(String sessionId) {
        redisTemplate.expire(sessionId, sessionTimeoutSecond, TimeUnit.SECONDS);
    }
}