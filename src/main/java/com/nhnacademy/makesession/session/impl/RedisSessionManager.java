package com.nhnacademy.makesession.session.impl;

import com.nhnacademy.makesession.session.SessionManager;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
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

    private final RedisTemplate<String, Object> redisTemplate;

    private long sessionCreateTime;

    private int sessionTimeoutSecond = 30 * 60;

    @Override
    public void createSession() {
        String sessionId = UUID.randomUUID().toString();

        Cookie sessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);

        ServletRequestAttributes servletAttributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletResponse response = Objects.requireNonNull(servletAttributes).getResponse();

        Objects.requireNonNull(response).addCookie(sessionCookie);

        sessionCreateTime = System.currentTimeMillis();

        redisTemplate.opsForHash().put(getId(), SESSION_COOKIE_NAME, "create");
        redisTemplate.expire(getId(), sessionTimeoutSecond, TimeUnit.SECONDS);

    }

    @Override
    public void setAttribute(String key, Object value) {
        redisTemplate.opsForHash().put(getId(), key, value);
    }

    @Override
    public Object getAttribute(String key) {
        return redisTemplate.opsForHash().get(getId(), key);
    }

    @Override
    public void removeAttribute(String key) {
        redisTemplate.opsForHash().delete(getId(), key);
    }

    @Override
    public boolean isNew() {
        return redisTemplate.opsForHash().entries(getId()).isEmpty();
    }

    @Override
    public void invalidate() {
        if (findCookie(SESSION_COOKIE_NAME) != null) {
            redisTemplate.delete(findCookie(SESSION_COOKIE_NAME).getValue());
        }
    }

    @Override
    public String getId() {
        if (findCookie(SESSION_COOKIE_NAME) == null) {
            return null;
        }

        return findCookie(SESSION_COOKIE_NAME).getValue();
    }

    @Override
    public long getCreationTime() {
        return sessionCreateTime;
    } //세션이 생성된 시간을 January 1 ,1970 GMT 부터 long 형 밀리세컨드 값으로 반환

//    @Override
//    public long getLastAccessedTime() {
//
//    }  //웹 브라우저의 요청이 마지막으로 시도된 시간을 long 형 ms 값으로 반환

    @Override
    public void setMaxInactiveInterval(int second) {
        this.sessionTimeoutSecond = second;
    }  //세션을 유지할 시간을 초단위로 설정 합니다.

    @Override
    public int getMaxInactiveInterval() {
        return this.sessionTimeoutSecond;
    } //세션의 유효시간을 초 단위로 반환 합니다. 기본값은 30초 입니다.

    @Override
    public Cookie findCookie(String cookieName) {
        ServletRequestAttributes servletAttributes =
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        HttpServletRequest request = Objects.requireNonNull(servletAttributes).getRequest();

        return Arrays.stream(request.getCookies())
                     .filter(cookie -> cookie.getName().equals(cookieName))
                     .findAny()
                     .orElse(null);
    }
}
