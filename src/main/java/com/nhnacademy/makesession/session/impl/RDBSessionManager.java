package com.nhnacademy.makesession.session.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.makesession.entity.Member;
import com.nhnacademy.makesession.entity.SessionId;
import com.nhnacademy.makesession.entity.SessionMap;
import com.nhnacademy.makesession.repository.SessionIdRepository;
import com.nhnacademy.makesession.repository.SessionMapRepository;
import com.nhnacademy.makesession.service.impl.SessionHolder;
import com.nhnacademy.makesession.session.SessionManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 *
 * @author : 유호철, 조재철
 * @since 1.0
 */
@Profile("rdb")
@Component
@RequiredArgsConstructor
@Log4j2
public class RDBSessionManager implements SessionManager {
    private final SessionIdRepository idRepository;

    private final SessionMapRepository mapRepository;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private int sessionTimeoutSecond = 30 * 60;
    private long sessionCreationTime;

    private long sessionFinalRequestTime;
    @Override
    public void setAttribute(String key, Object value) throws JsonProcessingException {
        SessionId sessionId = new SessionId(getId(), LocalDateTime.now().plusSeconds(sessionTimeoutSecond));
        idRepository.save(sessionId);
        String convertValue = objectMapper.writeValueAsString(value);
        log.error("object to json");
        log.error("{}",convertValue);
        SessionMap sessionMap = new SessionMap(sessionId, key, convertValue);
        sessionId.add(sessionMap);
    }

    @Override
    public Object getAttribute(String key) throws JsonProcessingException {
        SessionMap sessionMap = mapRepository.findByKey(key);
        Member member = objectMapper.readValue(sessionMap.getValue(), Member.class);
        log.error("json to object");
        log.error("{}",member.toString());
        return member;
    }

    @Override
    public void removeAttribute(String key) {
        mapRepository.deleteByKey(key);
    }

    @Override
    public boolean isNew() {
        return getId().isEmpty();
    }

    @Override
    public void invalidate() {
        Cookie sessionCookie = findCookie(SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            idRepository.deleteById(findCookie(SESSION_COOKIE_NAME).getValue());
            removeCookie(sessionCookie);
            SessionHolder.removeSessionId();
        }
    }

    @Override
    public String getId() {
        return SessionHolder.getSessionId();
    }

    @Override
    public long getCreationTime() {
        return this.sessionCreationTime;
    }

    @Override
    public void setCreationTime(long createtionTIme) {
        this.sessionCreationTime = createtionTIme;
    }

    @Override
    public long getLastAccessedTime() {
        return sessionFinalRequestTime;
    }

    @Override
    public void setLastAccessedTime(long currentTime) {
        this.sessionFinalRequestTime = currentTime;
    }

    @Override
    public void setMaxInactiveInterval(int second) {
        sessionTimeoutSecond = second;
    }

    @Override
    public int getMaxInactiveInterval() {
        return this.sessionTimeoutSecond;
    }

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
        idRepository.findById(sessionId).ifPresent(value -> value.addTime(sessionTimeoutSecond));
    }
}
